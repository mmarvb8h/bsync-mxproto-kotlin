package bsync

import io.ktor.application.log
import kotlinx.coroutines.channels.*
import io.ktor.http.cio.websocket.*
import io.ktor.websocket.*
import org.koin.core.KoinComponent
import org.koin.core.inject


object WsHandler : KoinComponent {

    suspend fun handleMsg(wsSession: WebSocketServerSession) {

        val log = wsSession.application.log

        try {
            val msgHandler: WsMessage by inject ()
            // Get a message from the session (called a frame). This frame
            // can be text, binary, a ping...
            frameBlock@
            for (frame in wsSession.incoming) {
                when (frame) {
                    is Frame.Text -> {
                        val reqText = frame.readText()
                        msgHandler.handleMessage(wsSession = wsSession, requestText = reqText)
                    }
                    else -> break@frameBlock
                }
            }
        } catch (e: ClosedReceiveChannelException) {
            // connection closed.
            log.debug("[Websocket Handler] session closed by client.")
        } catch (e: Throwable) {
            // catch crash occurred.
            log.info("*** <crash> Throwable [Websocket Handler] session crashed and burned.")
            log.debug("*** <crash exception msg> " + e.localizedMessage)
            // wsSession.close(CloseReason(CloseReason.Codes.UNEXPECTED_CONDITION, "Session crash."))
        }
    }
}
