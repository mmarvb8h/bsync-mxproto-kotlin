package bsync.banks.plaid

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import bsync.myhttp.WsClientSessionI
import org.jetbrains.exposed.sql.transactions.transaction
import bsync.db.models.Access
import org.jetbrains.exposed.sql.*


class AccessDb(val clientSession: WsClientSessionI) {

    // Since the below will use a std Java library for DB access, i wrap
    // and launch an IO coroutine to do this.

    suspend fun saveExchangeToken(data1: MyItemPubTokenExchangeReq): Unit =
        withContext(
            context = Dispatchers.IO + CoroutineName("SaveAccessToken")
        ) {

            clientSession.log.debug("Save access token in db.")

            // Save account data
            transaction {
                val existInTable =
                    Access.select {
                        exists(Access.select((Access.accessToken eq data1.accessToken)
                                and (Access.itemId eq data1.itemId)))
                    }.toList()

                if (existInTable.isEmpty()) {
                    val id = Access.insertAndGetId {
                        it[accessToken] = data1.accessToken
                        it[itemId] = data1.itemId
                        it[bankName] = ""
                        it[bankUid] = ""
                        it[profileId] = 1
                    }
                }

            } // End of transaction

        }
    
}
