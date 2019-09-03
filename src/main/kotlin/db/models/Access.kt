package bsync.db.models

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column


object Access : IntIdTable(name = "Access") {
    //val uid = varchar("uid", 60).uniqueIndex()
    val accessToken = varchar("access_token", 255)
    val itemId = varchar("item_id", 60)
    val bankName = varchar("bank_name", 255)
    val bankUid = varchar("bank_uid", 60)
    val profileId = integer("profile_id")
}