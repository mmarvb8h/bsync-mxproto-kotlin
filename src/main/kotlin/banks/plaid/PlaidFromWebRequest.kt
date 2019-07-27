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


object PlaidFromWebRequest : KoinComponent {

    // Since the below will use a std Java library calling the plaid web server, i wrap
    // and launch an IO coroutine to do this.

    suspend fun SyncAccountTransactions(
        client: MyPlaidClientXtend,
        exchangeToken: MyItemPubTokenExchangeReq,
        clientSession: WsClientSessionI,
        syncReq: BankSync) : Unit {

        // Will always save to db.

        if (syncReq.accountSyncGroup.accounts.isNullOrEmpty()) return

        val transaction: PlaidTransaction by inject {
            parametersOf(client, exchangeToken, clientSession) }
        val dataReceiver: PlaidTransactionDbModel by inject {
            parametersOf(clientSession) }

        // Setup date range. Set ending date.
        val ft = SimpleDateFormat("yyyy-MM-dd")
        val calender = Calendar.getInstance()
        // Get date from JSON request.
        val startDate = ft.parse(syncReq.accountSyncGroup.syncRange.startDate)
        calender.time = startDate
        // Increment from start date using num months.
        calender.add(Calendar.MONTH, syncReq.accountSyncGroup.syncRange.numMonths)
        // Setup list of accounts.
        var accountList = mutableListOf<String>()
        syncReq.accountSyncGroup.accounts.mapTo(accountList) { it.id }

        val param = GetTransactionParams(
            accountIds = accountList,
            startDate = startDate,
            endDate = calender.time
        )
        // Call to get data from Plaid.
        var transacData = transaction!!.getDataForAccount(param)
        dataReceiver!!.acceptGetAccountTransactionData(data1 = transacData.body)
    }
}


