package com.library.userservice.infrastructure.persistence

import com.library.userservice.domain.model.User
import com.library.userservice.domain.repository.UserRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepositoryImpl : UserRepository {
    override fun getAllUsers(): List<User> = transaction {
        Users.selectAll().map { row ->
            User(
                    id = row[Users.id],
                    username = row[Users.username],
                    email = row[Users.email],
                    fullName = row[Users.fullName]
            )
        }
    }

    override fun createUser(user: User): User = transaction {
        val id =
                Users.insert {
                    it[username] = user.username
                    it[email] = user.email
                    it[fullName] = user.fullName
                } get Users.id

        user.copy(id = id)
    }

    override fun updateUser(user: User): User = transaction {
        Users.update({ Users.id eq user.id }) {
            it[username] = user.username
            it[email] = user.email
            it[fullName] = user.fullName
        }
        user
    }

    override fun getUserById(id: Int): User? = transaction {
        Users.select { Users.id eq id }
                .map { row ->
                    User(
                            id = row[Users.id],
                            username = row[Users.username],
                            email = row[Users.email],
                            fullName = row[Users.fullName]
                    )
                }
                .singleOrNull()
    }
}
