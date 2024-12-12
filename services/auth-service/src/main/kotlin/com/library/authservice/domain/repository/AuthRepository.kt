package com.library.authservice.domain.repository

import com.library.authservice.domain.model.AuthUser

interface AuthRepository {
    suspend fun findByUsername(username: String): AuthUser?
    suspend fun save(user: AuthUser): AuthUser
}
