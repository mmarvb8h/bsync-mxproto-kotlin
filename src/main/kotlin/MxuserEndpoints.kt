package bsync

import bsync.banking.mx.MxUserConnectService
import bsync.banking.mx.MxUserCreateService
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
import io.ktor.application.log
//import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.util.pipeline.PipelineContext


@UseExperimental(kotlinx.serialization.ImplicitReflectionSerializer::class)
class MxuserEndpoints : KoinComponent {

    suspend fun create(pipeline: PipelineContext<Unit, ApplicationCall>) {

        val service: MxUserCreateService by inject ()

        val profileId_param = pipeline.context.parameters["profile"]
        if (profileId_param == null) {
            pipeline.context.respond(HttpStatusCode.BadRequest, "")
            return
        }

        val clientStuff = MyHttpClientSession(clientSession = pipeline)
        val newUser = service.begin(
            clientSession = clientStuff,
            finsyncProfileId = profileId_param.toInt())

        if (newUser != null) {
            @UseExperimental(kotlinx.serialization.UnstableDefault::class)
            val jsonResponse = Json.stringify(
                CreateUserResponse.serializer(),
                CreateUserResponse(userGuid = newUser.guid)
            )
            pipeline.context.respondText(jsonResponse, ContentType.Text.Html, HttpStatusCode.Created)
        }
        else
            pipeline.context.respond(HttpStatusCode.ExpectationFailed, "")
    }

    suspend fun createAndConnect(pipeline: PipelineContext<Unit, ApplicationCall>) {

        val createService: MxUserCreateService by inject ()
        val connectService: MxUserConnectService by inject ()

        val profileId_param = pipeline.context.parameters["profile"]
        if (profileId_param == null) {
            pipeline.context.respond(HttpStatusCode.BadRequest, "")
            return
        }

        val clientStuff = MyHttpClientSession(clientSession = pipeline)

        val newUser = createService.begin(
            clientSession = clientStuff,
            finsyncProfileId = profileId_param.toInt())
        if (newUser == null) {
            pipeline.context.respond(HttpStatusCode.ExpectationFailed, "")
            return
        }

        val connectWidgetData = connectService.begin(
            clientSession = clientStuff,
            user = newUser)
        if (connectWidgetData == null) {
            pipeline.context.respond(HttpStatusCode.ExpectationFailed, "")
            return
        }

        @UseExperimental(kotlinx.serialization.UnstableDefault::class)
        val jsonResponse = Json.stringify(
            CreateUserResponse.serializer(),
            CreateUserResponse(
                userGuid = connectWidgetData.guid,
                connectWidgetUrl = connectWidgetData.connectWidgetUrl,
                finsyncId = 2)
        )
        pipeline.context.respondText(jsonResponse, ContentType.Text.Html, HttpStatusCode.Created)
    }
}
