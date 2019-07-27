package bsync.myhttp

import org.slf4j.Logger


interface WsClientSessionI {
    val clientIdentifier : String?
    val log : Logger
    suspend fun sendToClient(msg: String)
}
