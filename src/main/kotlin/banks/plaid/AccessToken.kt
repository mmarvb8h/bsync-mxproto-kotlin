package bsync.banks.plaid

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.plaid.client.request.*
import bsync.myhttp.WsClientSessionI


class AccessToken {

    // Since the below will use a std Java library calling the plaid web server, i wrap
    // and launch an IO coroutine to do this.

    suspend fun getExchangeToken(client: MyPlaidClientXtend, linkToken: String, clientSession: WsClientSessionI): MyItemPubTokenExchangeReq? =
        withContext(
            context = Dispatchers.IO + CoroutineName("getAccessToken")) {

            val localTok = getExchaneTokenLocal()
            if (localTok != null) return@withContext localTok
            getExchangeTokenFromServer(
                client = client, linkToken = linkToken, clientSession = clientSession)
        }

    fun getExchaneTokenLocal(): MyItemPubTokenExchangeReq? {
        // Find in local data store.
        return null
    }

    fun getExchangeTokenFromServer(client: MyPlaidClientXtend, linkToken: String, clientSession: WsClientSessionI): MyItemPubTokenExchangeReq? {

        clientSession.log.debug("calling plaid for exchange token")
        // Use plaid's Java library to call Plaid.
        val response = client.plaidClient.service()
            .itemPublicTokenExchange(ItemPublicTokenExchangeRequest(linkToken)).execute()
        if (response.isSuccessful) {
            val respBody = response.body()
            return MyItemPubTokenExchangeReq(
                accessToken = respBody.accessToken,
                itemId = respBody.itemId,
                requestId = respBody.requestId)
        }
        return null
    }

}


