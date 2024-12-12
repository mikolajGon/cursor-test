package com.library.admin.services

import at.favre.lib.crypto.bcrypt.BCrypt
import com.library.admin.models.AuthUser
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

class UserService {
    private val client =
            KMongo.createClient(
                            System.getenv("MONGO_URL")
                                    ?: "mongodb://authuser:authpass@localhost:27017"
                    )
                    .coroutine
    private val database = client.getDatabase("authdb")
    private val users = database.getCollection<AuthUser>()

    suspend fun createUser(username: String, password: String, roles: List<String>) {
        val hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray())
        val user =
                AuthUser(
                        id = generateId(),
                        username = username,
                        password = hashedPassword,
                        roles = roles
                )
        users.insertOne(user)
    }

    suspend fun listUsers(): List<AuthUser> = users.find().toList()

    private fun generateId(): Int = (Math.random() * 1000000).toInt()
}
