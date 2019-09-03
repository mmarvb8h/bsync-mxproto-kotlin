package bsync.db.models

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction


object Schema {

    fun create() {

        transaction {
            SchemaUtils.drop(Transactions, Accounts, Access)
            SchemaUtils.create(Transactions, Accounts, Access)
            // I can do other stuff
        }
    }
}