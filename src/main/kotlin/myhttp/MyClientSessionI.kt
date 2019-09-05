package bsync.myhttp

import org.slf4j.Logger


interface MyClientSessionI {
    val log : Logger
    suspend fun sendMessageToClient(msg: String)
    suspend fun respondToClient(msg: String)
}
