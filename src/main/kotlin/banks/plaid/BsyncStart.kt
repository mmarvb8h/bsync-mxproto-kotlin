package bsync.banks.plaid

import kotlinx.serialization.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.text.SimpleDateFormat

import bsync.myconfig.*
import bsync.myhttp.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.coroutines.EmptyCoroutineContext


class BsyncStart : KoinComponent {

    suspend fun begin(clientSession: WsClientSessionI, syncReq: BankSync) {

        val clientId = Config.str()
        {
            Config.configValue(Config.KeyPaths.PLAID_CLIENTID)
        }
        val secretKey = Config.str()
        {
            Config.configValue(Config.KeyPaths.PLAID_SECRETKEY)
        }
        val pubKey = Config.str()
        {
            Config.configValue(Config.KeyPaths.PLAID_PUBKEY)
        }
        val linkToken = syncReq.linkToken ?: ""

        clientSession.log.debug("call syncRoutine")

        CoroutineScope(
            context = Dispatchers.Default + CoroutineName(name = "beginBankSync")
        ).launch {

            clientSession.log.debug("in coroutine 1")

            val clientFactory: PlaidClientFactory by inject()
            val myplaidClient = clientFactory!!.createWithXtendAccess(
                clientId = clientId,
                secretKey = secretKey,
                publicKey = pubKey)

            val plaidAccessTok: AccessToken by inject()
            val exchangeToken = plaidAccessTok!!.getExchangeToken(
                client = myplaidClient,
                linkToken = linkToken,
                clientSession = clientSession) ?: return@launch

            TransactionSyncWebRequest.SyncAccountTransactions(
                exchangeToken = exchangeToken,
                client = myplaidClient,
                clientSession = clientSession,
                syncReq = syncReq)

            clientSession.log.debug("in coroutine end")
        }
        clientSession.log.debug("call syncRoutine end")
    }

}




//suspend fun getAccesstoken(clientSession: WsClientSessionI, syncReq: BankSync) =
//    withContext(context = Dispatchers.IO + CoroutineName("test")) {
//        // Use plaid's Java library to call Plaid.
//    }

// Since this is being called from suspend function i'm going to try the withContext
// the withContext syntax. I would have to use the below and start a context
// if being called without a coroutine/suspend function.

//fun CoroutineScope.beginBankSyncB(clientSession: WsClientSessionI, syncReq: BankSync) =
//    launch(Dispatchers.IO) {
//
//    }
//
//private fun _onMessageHandler(ctx: WsMessageContext, msgctx: WsMessageCtx) =
//    CoroutineScope(EmptyCoroutineContext).launch {
