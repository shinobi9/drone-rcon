import ink.rubi.krcon.RCONClient
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val client = RCONClient()
        client.connect()
        client.login()
        repeat(10){
            client.sendCommand("say hello $it")
        }
        client.closeSocketAndSuicide()
    }
}