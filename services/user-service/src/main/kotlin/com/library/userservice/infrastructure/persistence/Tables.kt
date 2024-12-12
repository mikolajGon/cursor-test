package com.library.userservice.infrastructure.persistence

import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 50).uniqueIndex()
    val email = varchar("email", 255).uniqueIndex()
    val fullName = varchar("full_name", 255)

    override val primaryKey = PrimaryKey(id)
}
