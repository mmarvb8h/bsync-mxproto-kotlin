package bsync.myhttp

import io.ktor.websocket.*
import io.ktor.http.cio.websocket.*
import io.ktor.application.log
import kotlinx.serialization.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import kotlinx.serialization.json.Json

import bsync.myhttp.*
//import bsync.banks.plaid.*

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import org.slf4j.Logger

import java.time.LocalDateTime
import java.text.SimpleDateFormat


class WsClientSession (val clientSession: WebSocketServerSession
    ) : MyClientSessionI {

    val app = this.clientSession.application
    override val log = this.app.log

    override suspend fun sendMessageToClient(msg: String) {
        clientSession.send(msg)
    }
    override suspend fun respondToClient(msg: String) {}

}
