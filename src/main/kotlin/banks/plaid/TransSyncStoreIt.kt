package bsync.banks.plaid

import bsync.myhttp.WsClientSessionI
import com.plaid.client.*
import com.plaid.client.request.*
import com.plaid.client.response.ItemPublicTokenExchangeResponse
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Calendar


object TransSyncStoreIt : KoinComponent {

    // Since the below will use a std Java library calling the plaid web server, i wrap
    // and launch an IO coroutine to do this.

    suspend fun SyncAccountTransactions(
        client: MyPlaidClientXtend,
        exchangeToken: MyItemPubTokenExchangeReq,
        clientSession: WsClientSessionI,
        syncReq: TransSync) : Unit {

        // Will always save to db.

        if (syncReq.accountSyncGroup.accounts.isNullOrEmpty()) return

        val transaction: PlaidTrans by inject {
            parametersOf(client, exchangeToken, clientSession) }
        val dataReceiver: PlaidTransToSchema by inject {
            parametersOf(clientSession) }

        // Setup date range. Set ending date.
        val ft = SimpleDateFormat( "yyyy-MM-dd")
        // Setup list of accounts.
        var accountList = mutableListOf<String>()
        syncReq.accountSyncGroup.accounts.mapTo(accountList) { it.id }

        // Get pager to handle paging calls to Plaid.
        val pager = GetTransactionPager(
            accountIdList = accountList,
            startDate = ft.parse(syncReq.accountSyncGroup.syncRange.startDate),
            numMonths = syncReq.accountSyncGroup.syncRange.numMonths
        )
        // Call to get data from Plaid.
        var currPage = pager.start()
        while(pager.hasNextPage(currPage)) {
            val transacData = transaction.getDataForAccount(currPage)
            dataReceiver.saveTransactionGet(data1 = transacData.body)
            currPage = pager.nextPage(currPage, transacData.body)
        }
    }
}


