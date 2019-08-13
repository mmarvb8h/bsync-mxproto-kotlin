package bsync

import io.ktor.websocket.*
import io.ktor.http.cio.websocket.*
import io.ktor.application.log
import kotlinx.serialization.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import kotlinx.serialization.json.Json

import bsync.myhttp.*
import bsync.banks.plaid.*

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import org.slf4j.Logger

import java.time.LocalDateTime
import java.text.SimpleDateFormat


class WsClientStuff (val clientSession: WebSocketServerSession,
                     override val clientIdentifier : String?
) : WsClientSessionI {

    val app = this.clientSession.application
    override val log = this.app.log

    override suspend fun sendToClient(msg: String) {
        clientSession.send(msg)
    }
}


@UseExperimental(kotlinx.serialization.ImplicitReflectionSerializer::class)
class WsHandler : KoinComponent {

    suspend fun handleMessage(wsSession: WebSocketServerSession, requestText: String) {

        val log = wsSession.application.log
        log.debug("In websocket handler")

        // Deserialize json formatted msg.
        @UseExperimental(kotlinx.serialization.UnstableDefault::class)
        val reqMessage = Json.parse(WsRequest.serializer(), requestText)

        // Do we know who to send this to?
        if (reqMessage.sendTo == null) return

        val theSession = WsClientStuff(
            clientSession = wsSession, clientIdentifier = reqMessage.clientIdentifier)

        val resp = when (reqMessage.sendTo) {

            "who_am_i" -> {
                val whoAmIHandler: WhoAmI by inject ()
                whoAmIHandler.sendIdentity(
                    clientSession = theSession, request = reqMessage.message)
            }

            "plaid" -> {
                // Inject plaid manager.
                val plaidService: PlaidServiceI by inject ()
                plaidService.performTheService(
                    clientSession = theSession, request = reqMessage.message)
            }

            else -> log.debug("*** <websocket request unknown> " + reqMessage.sendTo)
        }

        log.debug("Leaving websocket handler")
    }
}
