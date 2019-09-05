package bsync

import bsync.myconfig.CommandLine
import bsync.myconfig.loadConfig
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.slf4j.LoggerFactory


fun main(args: Array<String>) {
    // Load our configuration from disk.
    mainClass.apply {
        loadAppConfig()
        loadCommandLine(args)
    }

    // Setup server connections.
    val env = applicationEngineEnvironment {
        module {
            main()
        }
        // Websocket API
        connector {
            host = "0.0.0.0"
            port = 5052
        }
        // Public API
        connector {
            host = "0.0.0.0"
            port = 5053
        }
        connector {
            host = "0.0.0.0"
            port = 8443
        }
//        sslConnector (
//            keyStore = "",
//            keyAlias = "",
//            keyStorePassword = { "".toCharArray() },
//            privateKeyPassword = { "".toCharArray() }
//        ) {
//            port = 8483
//            host = "0.0.0.0"
//            //keyStorePath = ""
//        }
    }

    embeddedServer(Netty, env).start()
}


object mainClass {

    val logger = LoggerFactory.getLogger("mymain")

    // Need to make any command line args available.
    fun loadCommandLine(args: Array<String>) {
        CommandLine(args)
    }

    // Load our configuration file from disk.
    fun loadAppConfig() {

        try {
            val contents = javaClass.getResource("/my-config.toml").readText()
            loadConfig(contents)
        } catch (e: Throwable) {
            this.logger.info("***crash> Could not load configuration. File may not exist in resource directory.")
        }
    }
}