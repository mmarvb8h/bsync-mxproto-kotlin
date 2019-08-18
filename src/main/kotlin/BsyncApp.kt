package bsync

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.websocket.*
import org.koin.Logger.SLF4JLogger
import org.koin.core.parameter.parametersOf
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import java.time.Duration
import bsync.db.postgres.Postgres
import bsync.db.models.Schema
import bsync.myconfig.CommandLine
import bsync.myhttp.ConnectorInspect

val x: Application.() -> Unit = {main()}

// Entry point.
fun Application.main() {
    // Left this here so i know how to define a variable of a class type.
    //val x: Application.() -> Unit = {main()}

    // Do first part of initialization.
    TheApp.apply {
        initLogging()
        loadInjectedComponents()
        initDb()
    }

    // Create schema on startup if requested.
    if (CommandLine.contains("createdb")) {
        // Generate schema.
        Schema.create()
    }

    // Initialize remainder of web application.
    TheApp.apply {
        initWeb()
        defineRoutes()
    }
}


object TheApp {

    fun Application.initLogging() {
        install(CallLogging)
    }

    fun Application.initDb() {
        Postgres.getConnection()
    }

    fun Application.initWeb() {
        install(DefaultHeaders)
        install(ConnectorInspect) {
            webSocketPort = 5052
            webAppPort = 5053
        }
        install(WebSockets) {
            pingPeriod = Duration.ofMinutes(1)
        }
    }

    fun Application.loadInjectedComponents() {
        // Use Koin to do dynamic loading.
        install(Koin) {
            SLF4JLogger(org.koin.core.logger.Level.INFO)
            modules(
                appModule,
                plaidModule)
        }
    }

    fun Application.defineRoutes() {
        // Lazy inject my websocket routing object.
        val webRouter: WebRouter? by inject { parametersOf(this) }

        install(Routing) {
            webRouter!!.webRoutes(this)
        }
    }
}
