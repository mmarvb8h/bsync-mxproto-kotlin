package bsync

import bsync.myconfig.loadConfig
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.slf4j.LoggerFactory


fun main(args: Array<String>) {
    // Load our configuration from disk.
    mainClass.loadAppConfig()

    embeddedServer(Netty, commandLineEnvironment(args)).start()
}


object mainClass {

    val logger = LoggerFactory.getLogger("mymain")

    fun loadAppConfig() {

        try {
            val contents = javaClass.getResource("/my-config.toml").readText()
            loadConfig(contents)
        } catch (e: Throwable) {
            this.logger.info("***crash> Could not load configuration. File may not exist in resource directory.")
        }
    }
}