package bsync.db.models

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column


object Transactions : IntIdTable(name = "Transactions") {
    val uid = varchar("uid", 60)
    val accountUid = varchar("account_uid", 60)
        .references(Accounts.uid)
    val date = date("posted_date")
    val name = varchar("name", 128)
    val amountCents = double("amount_cents")
    val categories = varchar("categories_list", 128)
}