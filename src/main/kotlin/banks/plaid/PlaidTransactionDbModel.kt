package bsync.banks.plaid

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.plaid.client.response.TransactionsGetResponse
import bsync.myhttp.WsClientSessionI


class PlaidTransactionDbModel(val clientSession: WsClientSessionI) {

    // Since the below will use a std Java library for DB access, i wrap
    // and launch an IO coroutine to do this.

    suspend fun acceptGetAccountTransactionData(data1: TransactionsGetResponse) : Unit =
        withContext(
            context = Dispatchers.IO + CoroutineName("acceptGetTransactionData")) {

            clientSession.log.debug("got me some data in dbModel.")

        }

    fun translate(data: String) : Unit {


    }
}


