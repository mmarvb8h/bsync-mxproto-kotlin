package bsync.db.models

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column


object AccountCategory : IntIdTable(name = "AccountCategories") {
    val categoryId = integer("category_id").uniqueIndex()
    val categories = varchar("categories_list", 128)
}
