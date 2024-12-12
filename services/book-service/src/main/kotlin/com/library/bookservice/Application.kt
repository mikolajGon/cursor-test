package com.library.bookservice

import com.library.bookservice.api.configureBookRoutes
import com.library.bookservice.application.di.bookModule
import com.library.bookservice.infrastructure.persistence.Books
import com.library.shared.tracing.TracingConfig
import com.library.shared.tracing.configureTracing
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.opentelemetry.api.OpenTelemetry
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main() {
    val openTelemetry = TracingConfig.initializeTracing("book-service")

    // Initialize database connection
    Database.connect(
            url = System.getenv("DATABASE_URL") ?: "jdbc:postgresql://localhost:5432/bookdb",
            driver = "org.postgresql.Driver",
            user = System.getenv("DATABASE_USER") ?: "bookuser",
            password = System.getenv("DATABASE_PASSWORD") ?: "bookpass"
    )

    transaction { SchemaUtils.create(Books) }

    embeddedServer(
                    Netty,
                    port = 8081,
                    host = "0.0.0.0",
                    watchPaths = listOf("classes", "resources")
            ) { module(openTelemetry) }
            .start(wait = true)
}

fun Application.module(openTelemetry: OpenTelemetry) {
    install(Koin) {
        slf4jLogger()
        modules(bookModule)
    }

    install(ContentNegotiation) { json() }

    configureTracing(openTelemetry)
    configureBookRoutes()
}
