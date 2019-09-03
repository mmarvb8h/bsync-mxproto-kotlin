package bsync.banks.plaid

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.plaid.client.response.TransactionsGetResponse
import bsync.myhttp.WsClientSessionI
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import bsync.db.models.Accounts
import bsync.db.models.Transactions
import org.joda.time.DateTime
import java.text.SimpleDateFormat


class PlaidTransToSchema(val clientSession: WsClientSessionI) {

    // Since the below will use a std Java library for DB access, i wrap
    // and launch an IO coroutine to do this.

    suspend fun saveTransactionGet(data1: TransactionsGetResponse): Unit =
        withContext(
            context = Dispatchers.IO + CoroutineName("saveTransactionGet")
        ) {

            val xx = data1
            clientSession.log.debug("got me some data in dbModel.")

            // Save account data
            transaction {

                data1.accounts.forEach {
                    val existInTable =
                        Accounts.select {
                            exists(Accounts.select(Accounts.uid eq it.accountId))
                        }.toList()

                    if (existInTable.isEmpty()) {
                        val account = it
                        val id = Accounts.insertAndGetId {
                            it[uid] = account.accountId
                            it[name] = account.officialName
                            it[type] = account.type
                            it[subtype] = account.subtype
                        }
                    }
                }

                data1.transactions.forEach {
                    val existInTable =
                        Transactions.select {
                            exists(Transactions.select(Transactions.uid eq it.transactionId))
                        }.toList()

                    if (existInTable.isEmpty()) {
                        val transaction = it
                        val id = Transactions.insertAndGetId {
                            it[uid] = transaction.transactionId
                            it[accountUid] = transaction.accountId
                            it[date] = DateTime(transaction.date)
                            it[name] = transaction.name
                            it[amountCents] = transaction.amount * 100
                            it[categories] = ""
                        }
                    }

                }
            } // End of transaction

        }

}
