package bsync.banks.plaid

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.text.SimpleDateFormat

import bsync.myconfig.*
import bsync.myhttp.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

import kotlin.coroutines.EmptyCoroutineContext


@UseExperimental(kotlinx.serialization.ImplicitReflectionSerializer::class)
class AccessWorker : KoinComponent {

    suspend fun create(clientSession: WsClientSessionI,
                       publicAccessKey: String,
                       finsyncProfileId: Int,
                       bankName: String?,
                       bankUid: String?) {

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

        CoroutineScope(
            context = Dispatchers.Default + CoroutineName(name = "Access Worker")
        ).launch {

            clientSession.log.debug("in Access Worker 1")

            val clientFactory: PlaidClientFactory by inject()
            val myplaidClient = clientFactory.createWithXtendAccess(
                clientId = clientId,
                secretKey = secretKey,
                publicKey = pubKey)

            val plaidAccessTok: AccessToken by inject()
            val exchangeToken = plaidAccessTok.getExchangeTokenFromServer(
                client = myplaidClient,
                publicAccessKey = publicAccessKey,
                clientSession = clientSession) ?: return@launch
            // Persist token in DB.
            val saveExchangeToDb: AccessDb by inject{
                parametersOf(clientSession) }
            saveExchangeToDb.saveExchangeToken(exchangeToken)

            // Send back response
            @UseExperimental(kotlinx.serialization.UnstableDefault::class)
            val jsonResponse = Json.stringify(
                AccessIdMessage.serializer(),
                AccessIdMessage(accessId = 1))

            clientSession.log.debug("in Access Worker end")
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
