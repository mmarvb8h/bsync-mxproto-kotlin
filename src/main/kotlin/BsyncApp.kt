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


// Entry point.
fun Application.main() {

    TheApp.apply {
        loadInjectedComponents()
        init()
        defineRoutes()
    }
}


object TheApp {

    fun Application.init() {
        install(DefaultHeaders)
        install(CallLogging)
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
        val webRouter: WebRouter by inject { parametersOf(this) }

        install(Routing) {
            webRouter!!.webRoutes(this)
        }
    }
}




