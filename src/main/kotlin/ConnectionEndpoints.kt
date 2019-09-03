package bsync

import io.ktor.http.*
import io.ktor.response.*
//import io.ktor.routing.*


import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import kotlinx.serialization.json.Json

import bsync.myhttp.*
import bsync.banks.plaid.*
import io.ktor.application.ApplicationCall
//import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.util.pipeline.PipelineContext


//class WsClientStuff (val clientSession: WebSocketServerSession,
//                     override val clientIdentifier : String?
//) : WsClientSessionI {
//
//    val app = this.clientSession.application
//    override val log = this.app.log
//
//    override suspend fun sendToClient(msg: String) {
//        clientSession.send(msg)
//    }
//}


@UseExperimental(kotlinx.serialization.ImplicitReflectionSerializer::class)
class ConnectionEndpoints : KoinComponent {

    suspend fun create(pipeline: PipelineContext<Unit, ApplicationCall>) {

        val plaidService: PlaidServiceI by inject ()

//        val theSession = WsClientStuff(
//            clientSession = wsSession, clientIdentifier = reqMessage.clientIdentifier)
//
//        plaidService.callservices(
//            clientSession = theSession, request = reqMessage.message)

        @UseExperimental(kotlinx.serialization.UnstableDefault::class)
        val jsonResponse = Json.stringify(
            AccessIdMessage.serializer(),
            AccessIdMessage(accessId = 1))

        pipeline.context.respondText(jsonResponse, ContentType.Text.Html)
        // log.debug("Leaving websocket msg handler")
    }
}
