package bsync.db.models

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction


object Schema {

    fun create() {

        transaction {
            SchemaUtils.drop(Transactions, Accounts)
            SchemaUtils.create(Transactions, Accounts)
            // I can do other stuff
        }
    }
}