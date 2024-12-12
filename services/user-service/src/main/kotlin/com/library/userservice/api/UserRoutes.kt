package com.library.userservice.api

import com.library.shared.tracing.TracingConfig
import com.library.userservice.domain.model.User
import com.library.userservice.domain.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.opentelemetry.api.trace.SpanKind
import io.opentelemetry.api.trace.StatusCode
import io.opentelemetry.api.trace.Tracer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.ktor.ext.inject

private suspend inline fun <reified T> handleTracedRequest(
        call: ApplicationCall,
        tracer: Tracer,
        spanName: String,
        errorStatus: HttpStatusCode = HttpStatusCode.InternalServerError,
        crossinline block: suspend () -> T
) {
    val span = tracer.spanBuilder(spanName).setSpanKind(SpanKind.SERVER).startSpan()
    try {
        withContext(Dispatchers.IO) {
            val result = block()
            when (result) {
                is Unit -> call.respond(HttpStatusCode.NoContent)
                else -> call.respond(result as Any)
            }
        }
        span.setStatus(StatusCode.OK)
    } catch (e: IllegalArgumentException) {
        span.setStatus(StatusCode.ERROR, e.message ?: "Validation error")
        withContext(Dispatchers.IO) {
            call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to (e.message ?: "Invalid request"))
            )
        }
    } catch (e: Exception) {
        span.setStatus(StatusCode.ERROR, e.message ?: "Unknown error")
        withContext(Dispatchers.IO) {
            call.respond(errorStatus, mapOf("error" to (e.message ?: "An error occurred")))
        }
    } finally {
        span.end()
    }
}

fun Application.configureUserRoutes() {
    val userService: UserService by inject()
    val tracer = attributes[TracingConfig.TRACER_KEY]

    routing {
        route("/users") {
            get { handleTracedRequest(call, tracer, "get-all-users") { userService.getAllUsers() } }

            post {
                handleTracedRequest(call, tracer, "create-user", HttpStatusCode.BadRequest) {
                    val user = call.receive<User>()
                    userService.createUser(user)
                }
            }
        }
    }
}
