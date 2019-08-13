package bsync.banks.plaid

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import com.plaid.client.request.*
import com.plaid.client.response.TransactionsGetResponse
import bsync.myhttp.WsClientSessionI


class PlaidTrans(val client: MyPlaidClientXtend,
                       val exchangeToken: MyItemPubTokenExchangeReq,
                       val clientSession: WsClientSessionI) : KoinComponent {

    // Since the below will use a std Java library calling the plaid web server, i wrap
    // and launch an IO coroutine to do this.

     suspend fun getDataForAccount(params: GetTransactionParams) : MyPlaidResponse<TransactionsGetResponse> {

        val transactionReq = prepareTransGetRequest(params)
        val result = callServer(transactionReq = transactionReq)
        // $$$ TODO Will need to consider what to do with error responses???
        return result
    }

    val prepareTransGetRequest = fun (params: GetTransactionParams) : TransactionsGetRequest {
        if (params.offSet == null || params.numPerPage == null) {
            // Return with no offset.
            return TransactionsGetRequest(
                exchangeToken.accessToken,
                params.startDate,
                params.endDate)
                .withAccountIds(params.accountIds)
        }
        // Return with offset.
        return TransactionsGetRequest(
            exchangeToken.accessToken,
            params.startDate,
            params.endDate)
            .withAccountIds(params.accountIds)
            .withOffset(params.offSet)
            .withCount(params.numPerPage)
    }

    suspend fun callServer(transactionReq: TransactionsGetRequest): MyPlaidResponse<TransactionsGetResponse> =
        withContext(
            context = Dispatchers.IO + CoroutineName("getTransactions")) {

        val response = client.plaidClient.service()
            .transactionsGet(transactionReq)
            .execute()

        clientSession.log.debug("got response from Plaid server, transactionGet().")

        MyPlaidResponse(
            code = response.code(),
            isSuccessful = response.isSuccessful,
            body = response.body(),
            message = response.message(),
            errorBody = response.errorBody()
        )
    }


}


