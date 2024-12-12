package com.library.userservice.domain.repository

import com.library.userservice.domain.model.User

interface UserRepository {
    fun getAllUsers(): List<User>
    fun createUser(user: User): User
    fun updateUser(user: User): User
    fun getUserById(id: Int): User?
}
