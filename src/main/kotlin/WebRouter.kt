package bsync

import io.ktor.application.Application
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.websocket.webSocket
import org.koin.core.KoinComponent
import org.koin.core.inject


class WebRouter (val app: Application) : KoinComponent {
    val env = this.app.environment

    fun webRoutes(routing: Routing) {

        try {
            routing.apply {

                webSocket("/ws") {
                    WsHandler.handleMsg(wsSession = this)
                }

                get("/mxuser/create") {
                    val endpoint: MxuserEndpoints by inject ()
                    endpoint.create(pipeline = this)
                }

                get("/mxuser/createandconnect") {
                    val endpoint: MxuserEndpoints by inject ()
                    endpoint.createAndConnect(pipeline = this)
                }

                get("/version") {
                    this.context.respondText(versionText(), ContentType.Text.Html)
                    //call.respondText(e.localizedMessage,ContentType.Text.Plain, HttpStatusCode.InternalServerError)
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


