package bsync.banking.mx

import kotlinx.coroutines.*

import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.text.SimpleDateFormat

import bsync.myhttp.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import com.mx.atrium.*
import com.mx.model.*
import com.mx.model.UserResponseBody




@UseExperimental(kotlinx.serialization.ImplicitReflectionSerializer::class)
class MxUserCreateService : KoinComponent {

//    suspend fun beginAsync(clientSession: WsClientSessionI,
//                       finsyncProfileId: Int) {
//
//
//        CoroutineScope(
//            context = Dispatchers.Default + CoroutineName(name = "Access Worker")
//        ).launch {
//
//            clientSession.log.debug("in Access Worker 1")
//
//            val newUser = begin(clientSession = clientSession, finsyncProfileId = finsyncProfileId)
//            // Need to respond to request.

//            clientSession.log.debug("call syncRoutine end")
//        }
//    }

    suspend fun begin(clientSession: MyClientSessionI,
                      finsyncProfileId: Int) : User? {

        val clientFactory: MxClientFactory by inject()
        val client = clientFactory.getClient()

        val userOptions = User()
        userOptions.identifier = finsyncProfileId.toString()

        clientSession.log.debug("Call MX to save.")
        return saveInMx(client = client, userOptions = userOptions)
    }

    suspend fun saveInMx(client: AtriumClient, userOptions: User): User? =
        withContext(
            context = Dispatchers.IO + CoroutineName("saveInMx")
        ) {

                val options = UserCreateRequestBody()
                options.user = userOptions
                // Call MX.
                val response = client.users.createUser(options)
                return@withContext response.user
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
