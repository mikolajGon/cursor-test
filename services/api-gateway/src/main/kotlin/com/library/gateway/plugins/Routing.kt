package com.library.gateway.plugins

import com.library.shared.tracing.TracingConfig
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.opentelemetry.api.trace.SpanKind
import io.opentelemetry.api.trace.StatusCode
import io.opentelemetry.api.trace.Tracer

private suspend fun handleServiceRequest(
        call: ApplicationCall,
        tracer: Tracer,
        spanName: String,
        serviceCall: suspend () -> HttpResponse
) {
    val span = tracer.spanBuilder(spanName).setSpanKind(SpanKind.SERVER).startSpan()
    try {
        val response = serviceCall()
        call.respond(response.status, response.bodyAsText())
        span.setStatus(StatusCode.OK)
    } catch (e: Exception) {
        span.setStatus(StatusCode.ERROR, e.message ?: "Unknown error")
        call.respond(HttpStatusCode.InternalServerError)
    } finally {
        span.end()
    }
}

fun Application.configureRouting() {
    val client = HttpClient(CIO) { install(ContentNegotiation) { json() } }
    val authServiceUrl = System.getenv("AUTH_SERVICE_URL") ?: "http://localhost:8083"
    val bookServiceUrl = System.getenv("BOOK_SERVICE_URL") ?: "http://localhost:8081"
    val userServiceUrl = System.getenv("USER_SERVICE_URL") ?: "http://localhost:8082"
    val tracer = attributes[TracingConfig.TRACER_KEY]

    routing {
        post("/api/auth/login") {
            handleServiceRequest(call, tracer, "gateway-auth-login") {
                val body = call.receiveText()
                client.post("$authServiceUrl/auth/login") {
                    setBody(body)
                    contentType(ContentType.Application.Json)
                }
            }
        }

        authenticate("auth-jwt") {
            route("/api") {
                route("/books") {
                    get {
                        handleServiceRequest(call, tracer, "gateway-get-books") {
                            client.get("$bookServiceUrl/books")
                        }
                    }

                    get("/{id}") {
                        handleServiceRequest(call, tracer, "gateway-get-book") {
                            val id = call.parameters["id"]
                            client.get("$bookServiceUrl/books/$id")
                        }
                    }

                    post {
                        handleServiceRequest(call, tracer, "gateway-create-book") {
                            val body = call.receiveText()
                            client.post("$bookServiceUrl/books") {
                                setBody(body)
                                contentType(ContentType.Application.Json)
                            }
                        }
                    }
                }

                route("/users") {
                    get {
                        handleServiceRequest(call, tracer, "gateway-get-users") {
                            client.get("$userServiceUrl/users")
                        }
                    }

                    get("/{id}") {
                        handleServiceRequest(call, tracer, "gateway-get-user") {
                            val id = call.parameters["id"]
                            client.get("$userServiceUrl/users/$id")
                        }
                    }

                    post {
                        handleServiceRequest(call, tracer, "gateway-create-user") {
                            val body = call.receiveText()
                            client.post("$userServiceUrl/users") {
                                setBody(body)
                                contentType(ContentType.Application.Json)
                            }
                        }
                    }
                }
            }
        }
    }
}
