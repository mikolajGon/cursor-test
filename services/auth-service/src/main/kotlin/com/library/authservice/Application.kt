package com.library.authservice

import com.library.authservice.api.configureAuthRoutes
import com.library.authservice.application.di.authModule
import com.library.shared.tracing.TracingConfig
import com.library.shared.tracing.configureTracing
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.opentelemetry.api.OpenTelemetry
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main() {
    val openTelemetry = TracingConfig.initializeTracing("auth-service")
    embeddedServer(
                    Netty,
                    port = 8083,
                    host = "0.0.0.0",
                    watchPaths = listOf("classes", "resources")
            ) { module(openTelemetry) }
            .start(wait = true)
}

fun Application.module(openTelemetry: OpenTelemetry) {
    install(Koin) {
        slf4jLogger()
        modules(authModule)
    }

    install(ContentNegotiation) { json() }

    configureTracing(openTelemetry)
    configureAuthRoutes()
}
