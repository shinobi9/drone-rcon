package ink.rubi.krcon

import kotlinx.coroutines.runBlocking

object DroneMain {
    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        val client = RCONClient()
        client.connect()
        if (client.config.password.isNotEmpty()) {
            client.login()
        }
        client.config.commands!!.split(",").forEach { client.sendCommand(it) }
        client.closeSocketAndSuicide()
    }
}