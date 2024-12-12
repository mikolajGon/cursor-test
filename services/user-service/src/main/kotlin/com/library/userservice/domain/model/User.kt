package com.library.userservice.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(val id: Int = 0, val username: String, val email: String, val fullName: String)
