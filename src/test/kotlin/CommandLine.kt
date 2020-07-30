import ink.rubi.krcon.RCONClient
import kotlinx.coroutines.runBlocking

object CommandLine {
    @JvmStatic
    fun main(args: Array<String>) {
        runBlocking {
            val client =  RCONClient()
            client.connect()
            client.login()
            while (true){
                client.sendCommand(readLine()!!)
            }
        }
    }
}


//fun ByteArray.prettyPrint() {
//    this.joinToString(separator = " ") {
//        val b = it.toString(16)
//        if (b.length == 1) "0$b" else b
//    }.let {
//        var index = 0
//        val length = 48
//        while (true) {
//            val max = index + length
//            if (it.length < max) {
//                println(it.substring(index, it.length))
//                break
//            } else {
//                println(it.substring(index, max))
//            }
//            index += length
//        }
//    }
//}