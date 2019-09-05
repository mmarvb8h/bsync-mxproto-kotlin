package bsync.myhttp

import io.ktor.application.ApplicationCall
import io.ktor.application.log
import io.ktor.util.pipeline.PipelineContext


class MyHttpClientSession (val clientSession: PipelineContext<Unit, ApplicationCall>
    ) : MyClientSessionI {

    val context = clientSession.context
    val app = context.application
    override val log = app.log

    override suspend fun sendMessageToClient(msg: String) {}
    override suspend fun respondToClient(msg: String) {}
}
