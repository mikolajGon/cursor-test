package com.library.bookservice.infrastructure.persistence

import org.jetbrains.exposed.sql.Table

object Books : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 255)
    val author = varchar("author", 255)
    val isbn = varchar("isbn", 13).uniqueIndex()

    override val primaryKey = PrimaryKey(id)
}
