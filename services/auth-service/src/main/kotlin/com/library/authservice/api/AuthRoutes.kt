package com.library.authservice.api

import com.library.authservice.domain.service.AuthService
import com.library.shared.tracing.TracingConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.opentelemetry.api.trace.SpanKind
import io.opentelemetry.api.trace.StatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

fun Application.configureAuthRoutes() {
    val authService: AuthService by inject()
    val tracer = attributes[TracingConfig.TRACER_KEY]

    routing {
        post("/auth/login") {
            val span = tracer.spanBuilder("auth-login").setSpanKind(SpanKind.SERVER).startSpan()
            try {
                withContext(Dispatchers.IO) {
                    val credentials = call.receive<LoginRequest>()
                    val token = authService.authenticate(credentials.username, credentials.password)
                    call.respond(mapOf("token" to token))
                }
                span.setStatus(StatusCode.OK)
            } catch (e: Exception) {
                span.setStatus(StatusCode.ERROR, e.message ?: "Unknown error")
                withContext(Dispatchers.IO) {
                    call.respond(
                            HttpStatusCode.Unauthorized,
                            mapOf("error" to "Invalid credentials")
                    )
                }
            } finally {
                span.end()
            }
        }
    }
}

@Serializable data class LoginRequest(val username: String, val password: String)
