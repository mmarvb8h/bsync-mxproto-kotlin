package bsync.db.models

import org.jetbrains.exposed.dao.IntIdTable


object Accounts : IntIdTable(name = "Accounts") {
    val uid = varchar("uid", 60).uniqueIndex()
    val name = varchar("name", 255)
    val type = varchar("type", 30)
    val subtype = varchar("subtype", 30)
}