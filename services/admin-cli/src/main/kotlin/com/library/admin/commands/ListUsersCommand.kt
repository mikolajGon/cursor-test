package com.library.admin.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.library.admin.services.UserService
import kotlinx.coroutines.runBlocking

class ListUsersCommand : CliktCommand(name = "list-users", help = "List all users") {
    override fun run() = runBlocking {
        val userService = UserService()
        val users = userService.listUsers()
        users.forEach { user ->
            echo("ID: ${user.id}, Username: ${user.username}, Roles: ${user.roles.joinToString()}")
        }
    }
}
