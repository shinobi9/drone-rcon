package ink.rubi.krcon

import kotlinx.coroutines.runBlocking

object DroneMain {
    @JvmStatic
    fun main(args: Array<String>) = runBlocking {
        val commands = System.getenv("RCON_COMMANDS")
        val client = RCONClient()
        client.connect()
        if (client.config.password.isNotEmpty()) {
            client.login()
        }
        commands.lines().forEach { client.sendCommand(it) }
        client.closeSocketAndSuicide()
    }
}