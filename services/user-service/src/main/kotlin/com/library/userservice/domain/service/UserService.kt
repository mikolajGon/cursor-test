package com.library.userservice.domain.service

import com.library.userservice.domain.model.User
import com.library.userservice.domain.repository.UserRepository

class UserService(private val repository: UserRepository) {
    fun getAllUsers(): List<User> = repository.getAllUsers()

    fun createUser(user: User): User {
        if (user.username.isBlank() || user.email.isBlank()) {
            throw IllegalArgumentException("Username and email are required")
        }
        return repository.createUser(user)
    }
}
