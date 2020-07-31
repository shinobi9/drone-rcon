package ink.rubi.krcon

import java.lang.System.getenv

data class RCONClientConfig(
    var host: String = "127.0.0.1",
    var port: Int = 25575,
    var socketTimeout: Long = 30_000,
    var password: String = "",
    var args: List<String> = listOf(),
    var commands: String? = null
) {
    init {
//        resolvePropertiesFromEnvironmentVariables()
//        resolvePropertiesFromProgramArguments()
        resolvePropertiesFromDronePluginSettings()
    }

    private fun resolvePropertiesFromEnvironmentVariables() {
        getenv("RCON_HOST")?.let { host = it }
        getenv("RCON_PORT")?.toIntOrNull()?.let { port = it }
        getenv("RCON_TIMEOUT")?.toLongOrNull()?.let { socketTimeout = it }
        getenv("RCON_PASSWORD")?.let { password = it }
        getenv("RCON_COMMANDS")?.let { commands = it }
    }

    private fun resolvePropertiesFromProgramArguments() {
//todo
    }

    private fun resolvePropertiesFromDronePluginSettings() {
        getenv("PLUGIN_HOST")?.let { host = it }
        getenv("PLUGIN_PORT")?.toIntOrNull()?.let { port = it }
        getenv("PLUGIN_TIMEOUT")?.toLongOrNull()?.let { socketTimeout = it }
        getenv("PLUGIN_PASSWORD")?.let { password = it }
        getenv("PLUGIN_COMMANDS")?.let { commands = it }
    }
}
