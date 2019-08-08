package bsync

import io.ktor.application.Application
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.websocket.webSocket


class WebRouter(val app: Application) {
    val env = this.app.environment

    fun webRoutes(routing: Routing) {
        try {
            routing.apply {

                webSocket("/ws") {
                    WsMessage.handleMsg(wsSession = this)
                }

                get("/version") {
                    this.context.respondText(versionText(), ContentType.Text.Html)
                }

                get("/dbcreate") {
                    this.context.respondText(versionText(), ContentType.Text.Html)
                }

                get("/") {
                    this.context.respondText("root path request received.")
                }
            }
        } catch (e: Throwable) {
            // catch unhandled crash.
            env.log.info("***crash> Throwable - web request crashed and burned.")
            env.log.info("***crash msg> " + e.localizedMessage)
        }
    }

}


