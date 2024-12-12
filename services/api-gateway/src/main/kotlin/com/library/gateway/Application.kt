package com.library.gateway

import com.library.gateway.plugins.*
import com.library.gateway.security.configureJwt
import com.library.shared.tracing.TracingConfig
import com.library.shared.tracing.configureTracing
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.opentelemetry.api.OpenTelemetry

fun main() {
    val openTelemetry = TracingConfig.initializeTracing("api-gateway")
    embeddedServer(
                    Netty,
                    port = 8080,
                    host = "0.0.0.0",
                    watchPaths = listOf("classes", "resources")
            ) { module(openTelemetry) }
            .start(wait = true)
}

fun Application.module(openTelemetry: OpenTelemetry) {
    configureLogging()
    configureTracing(openTelemetry)
    log.info("Starting API Gateway configuration")
    install(ContentNegotiation) { json() }
    configureJwt()
    log.info("JWT configuration completed")
    configureSecurity()
    configureHTTP()
    log.info("CORS configuration completed")
    configureRouting()
    log.info("Routing configuration completed")
}
