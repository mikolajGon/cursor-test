package com.library.admin.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthUser(
        val id: Int,
        val username: String,
        val password: String,
        val roles: List<String>
)
