package bsync.banks.plaid

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.plaid.client.request.*
import bsync.myhttp.MyClientSessionI


class AccessToken {

    // Since the below will use a std Java library calling the plaid web server, i wrap
    // and launch an IO coroutine to do this.

//    suspend fun getExchangeTokenFromServer(client: MyPlaidClientXtend, publicAccessKey: String, clientSession: WsClientSessionI): MyItemPubTokenExchangeReq? =
//        withContext(
//            context = Dispatchers.IO + CoroutineName("getAccessToken")) {
//
//            clientSession.log.debug("calling plaid for exchange token")
//            // Use plaid's Java library to call Plaid.
//            val response = client.plaidClient.service()
//                .itemPublicTokenExchange(ItemPublicTokenExchangeRequest(publicAccessKey)).execute()
//
//            if (response.isSuccessful) {
//                val respBody = response.body()
//                return@withContext MyItemPubTokenExchangeReq(
//                    accessToken = respBody.accessToken,
//                    itemId = respBody.itemId,
//                    requestId = respBody.requestId)
//            }
//            return@withContext null
//        }

}


