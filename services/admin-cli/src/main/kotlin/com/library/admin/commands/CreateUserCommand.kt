package com.library.admin.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.library.admin.services.UserService
import kotlinx.coroutines.runBlocking

class CreateUserCommand : CliktCommand(name = "create-user", help = "Create a new user") {
    private val username by option("-u", "--username", help = "Username").required()
    private val password by option("-p", "--password", help = "Password").required()
    private val roles by option("-r", "--roles", help = "Comma-separated roles").required()

    override fun run() = runBlocking {
        val userService = UserService()
        userService.createUser(username, password, roles.split(","))
        echo("User created successfully")
    }
}
