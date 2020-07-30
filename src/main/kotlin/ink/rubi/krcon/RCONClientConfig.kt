package ink.rubi.krcon

import java.lang.System.getenv

data class RCONClientConfig(
    var host: String = "127.0.0.1",
    var port: Int = 25575,
    var socketTimeout: Long = 30_000,
    var password: String = "",
    var args: List<String> = listOf(),
) {
    init {
//        resolvePropertiesFromEnvironmentVariables()
        resolvePropertiesFromProgramArguments()
    }

    private fun resolvePropertiesFromEnvironmentVariables() {
        //
        TODO()
    }

    private fun resolvePropertiesFromProgramArguments() {
        getenv("RCON_HOST")?.let { host = it }
        getenv("RCON_PORT")?.toIntOrNull()?.let { port = it }
        getenv("RCON_TIMEOUT")?.toLongOrNull()?.let { socketTimeout = it }
        getenv("RCON_PASSWORD")?.let { password = it }
    }

}
