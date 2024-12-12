package com.library.admin

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.library.admin.commands.CreateUserCommand
import com.library.admin.commands.ListUsersCommand

class AdminCli : CliktCommand() {
    override fun run() = Unit
}

fun main(args: Array<String>) =
        AdminCli().subcommands(CreateUserCommand(), ListUsersCommand()).main(args)
