package com.library.authservice.infrastructure.persistence

import com.library.authservice.domain.model.AuthUser
import com.library.authservice.domain.repository.AuthRepository
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class AuthRepositoryImpl(private val database: CoroutineDatabase) : AuthRepository {
    private val users = database.getCollection<AuthUser>()

    override suspend fun findByUsername(username: String): AuthUser? =
            users.findOne(AuthUser::username eq username)

    override suspend fun save(user: AuthUser): AuthUser {
        users.insertOne(user)
        return user
    }
}
