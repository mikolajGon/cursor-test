package com.library.userservice

import com.library.shared.tracing.TracingConfig
import com.library.shared.tracing.configureTracing
import com.library.userservice.api.configureUserRoutes
import com.library.userservice.application.di.userModule
import com.library.userservice.infrastructure.persistence.Users
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.opentelemetry.api.OpenTelemetry
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main() {
    val openTelemetry =
            TracingConfig.initializeTracing("user-service") // Initialize database connection
    Database.connect(
            url = System.getenv("DATABASE_URL")
                            ?: "jdbc:postgresql://localhost:5432/userdb?currentSchema=public",
            driver = "org.postgresql.Driver",
            user = System.getenv("DATABASE_USER") ?: "useruser",
            password = System.getenv("DATABASE_PASSWORD") ?: "userpass"
    )

    transaction { SchemaUtils.create(Users) }

    embeddedServer(
                    Netty,
                    port = 8082,
                    host = "0.0.0.0",
                    watchPaths = listOf("classes", "resources")
            ) { module(openTelemetry) }
            .start(wait = true)
}

fun Application.module(openTelemetry: OpenTelemetry) {
    install(Koin) {
        slf4jLogger()
        modules(userModule)
    }

    install(ContentNegotiation) { json() }
    configureTracing(openTelemetry)
    configureUserRoutes()
}
