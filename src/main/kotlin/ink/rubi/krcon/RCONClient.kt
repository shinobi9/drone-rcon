package ink.rubi.krcon

import ink.rubi.krcon.protocol.AtomicIntIdGenerator
import ink.rubi.krcon.protocol.RCONPacket
import ink.rubi.krcon.protocol.RCONPacket.PacketType.*
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.*
import io.ktor.utils.io.*
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress
import java.nio.ByteOrder
import kotlin.coroutines.CoroutineContext

class RCONClient(val config: RCONClientConfig = RCONClientConfig()) : CoroutineScope {
    private val log = LoggerFactory.getLogger("[RCON Client]")
    private val idGenerator = AtomicIntIdGenerator()
    private lateinit var socket: Socket
    private lateinit var input: ByteReadChannel
    private lateinit var output: ByteWriteChannel
    private val decodeCharset = Charsets.UTF_8
    private val encodeCharset = Charsets.UTF_8
    private val job = Job()
    private val exceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { ctx: CoroutineContext, t: Throwable ->
            log.error("catch exception in context : $ctx", t)
        }
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job + exceptionHandler
    private val keepListenResponse = atomic(true)
    private val keepListenCommands = atomic(true)
    private val packetQueue = Channel<RCONPacket>()
    private val idle = atomic(true)
    private val authenticated = atomic(false)

    @OptIn(KtorExperimentalAPI::class)
    suspend fun connect() {
        log.info("connect...")
        with(config) {
            socket = aSocket(ActorSelectorManager(Dispatchers.IO))
                .tcp()
                .connect(InetSocketAddress(config.host, config.port)) {
                    socketTimeout = config.socketTimeout
                }
        }

        input = socket.openReadChannel()
        output = socket.openWriteChannel(autoFlush = true)
        listenResponse()
        listenCommands()
    }

    private fun listenCommands() = launch {
        log.info("do listen commands...")
        while (true) {
            if (!keepListenCommands.value) break
            if (!idle.value) continue
            val packet = packetQueue.receive()
            output.writeRCONPacket(packet)
            idle.value = false
        }
    }

    suspend fun login() {
        log.info("login...")
        packetQueue.send(RCONPacket(config.password, SERVERDATA_AUTH, idGenerator.generateId(), encodeCharset))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun closeSocketAndSuicide() {
        while (true) {
            if (!packetQueue.isEmpty) continue
            log.info("closing...")
            withContext(Dispatchers.IO) { socket.close() }
            job.cancel()
            break;
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun sendCommand(command: String) {
        log.info("send command : $command")
        packetQueue.send(RCONPacket(command, SERVERDATA_EXECCOMMAND, idGenerator.generateId(), encodeCharset))
    }

    private fun listenResponse() = launch {
        log.info("do listen response...")
        while (keepListenResponse.value) {
            val packet = decode()
            handlePacket(packet)
        }
    }

    private fun handlePacket(packet: RCONPacket) {
        when (packet.type) {
            SERVERDATA_RESPONSE_VALUE.value -> {
                log.debug("id : ${packet.id} , type : COMMAND ")
                if (packet.body.size == 1)
                    log.info("response : empty")
                else
                    log.info("response ：${packet.body.showContent()}")
            }
            SERVERDATA_AUTH_RESPONSE.value -> {
                log.debug("id : ${packet.id} , type : AUTH ")
                if (packet.id == -1)
                    log.info("login failed ,wrong password")
                else {
                    authenticated.value = true
                    log.info("login success")
                }

            }
            else -> {
                log.info("unknown packet type ：${packet.type}")
            }
        }
        idle.value = true
    }

    private fun ByteArray.showContent() = String(this, 0, size - 1, decodeCharset)
    private suspend fun decode() = RCONPacket().apply {
        size = input.readIntLittleEndian()
        id = input.readIntLittleEndian()
        type = input.readIntLittleEndian()
        body = ByteArray(size - 9).also { input.readFully(it) }
        input.readByte()
    }
}

suspend fun ByteWriteChannel.writeRCONPacket(packet: RCONPacket) = write {
    it.order(ByteOrder.LITTLE_ENDIAN)
    it.putInt(packet.size)
    it.putInt(packet.id)
    it.putInt(packet.type)
    it.put(packet.body)
    it.put(packet.emptyString)
}