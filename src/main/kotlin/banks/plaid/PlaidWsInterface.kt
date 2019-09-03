package bsync.banks.plaid

import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlinx.serialization.json.Json
import bsync.myhttp.*

import kotlinx.serialization.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import java.time.LocalDateTime
import java.text.SimpleDateFormat
import bsync.myconfig.*


class PlaidWsInterface : KoinComponent {

    suspend fun callService(clientSession: WsClientSessionI, request: WsRequestMessage?) {
        if (request == null) return
        // Copy out of Ws (ie. websocket) format.
        when (request.messageKind) {
            "account_sync_start" -> {
                if (request.body == null) return
                // Deserialize json formatted msg.
                @UseExperimental(kotlinx.serialization.UnstableDefault::class)
                val bankSyncReq = Json.parse(TransSync.serializer(), request.body)

                val syncStartService: TransSyncStart by inject ()
                syncStartService.begin(clientSession = clientSession, syncReq = bankSyncReq)
            }
        }
    }
}

//    suspend fun AccountSync(bodyType: String?, requestBody: String?) {
//
//        val _session = this.session
//
//        val job = GlobalScope.launch {
//            if (isActive) {
//                _session.logDbg("Tee Hee in SyncAccount is Active coroutine")
//            }
//        }
//
//    }

//suspend fun SyncAccount(session: MyClientSessionI, bodyType: String?, requestBody: String?) = GlobalScope.launch {
//    delay(600L)
//    val websockSession = session.clientSession
//    websockSession.call.application.environment.log.debug("Tee Hee in SyncAccount coroutine")
//
////    val t0 = myconfig[Basic.nbr]
////    val t1 = myconfig[Basic.plaidEnvironment]
//    // ++++++++++++++
//    // Cancellation is cooperative. Check if i can continue.
//    if (isActive) {
//        websockSession.call.application.environment.log.debug("Tee Hee in SyncAccount is Active coroutine")
//    }
//    // Another way to check if i can continue. Note that this is also used to
//    // allow other coroutines/threads to run.
//    // yield()
//    // ++++++++++++++
//}
//
//suspend fun startAccountSync(session: MyClientSessionI) {
//    // get PlaidClient from Plaid library.
//    // get access token
//    //      look to see if one is saved in db.
//    //      if not found in db, get from Plaid.
//    // send progress to client...
//    // do some stuff...
//    // send progress to ciient...
//    // send final response to client...
//    // drop out...
//}

    //return Json.stringify(vInfo)
    //return ""