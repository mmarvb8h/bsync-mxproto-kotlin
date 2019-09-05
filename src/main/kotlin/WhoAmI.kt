package bsync

import kotlinx.serialization.json.Json
import java.util.UUID
import bsync.myhttp.*


@UseExperimental(kotlinx.serialization.ImplicitReflectionSerializer::class)
class WhoAmI {

    suspend fun sendIdentity(clientSession: MyClientSessionI, request: WsRequestMessage?) {

        clientSession.log.debug("In WhoAmI handler")

        // Serialize message body contents.
        @UseExperimental(kotlinx.serialization.UnstableDefault::class)
        val msgBodyJson = Json.stringify(
            WhoAmIMessage.serializer(),
            WhoAmIMessage(identification = UUID.randomUUID().toString()))
        // Serialize response object.
        @UseExperimental(kotlinx.serialization.UnstableDefault::class)
        val response = Json.stringify(
            WsResponse.serializer(),
            WsResponse(
                sendTo = request?.responseTo,
                message = WsResponseMessage(body = msgBodyJson)))

        clientSession.sendMessageToClient(response)
    }
}
