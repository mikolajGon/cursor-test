package com.library.bookservice.api

import com.library.bookservice.domain.model.Book
import com.library.bookservice.domain.service.BookService
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
import org.koin.ktor.ext.inject

fun Application.configureBookRoutes() {
    val bookService: BookService by inject()
    val tracer = attributes[TracingConfig.TRACER_KEY]

    routing {
        route("/books") {
            get {
                val span =
                        tracer.spanBuilder("get-all-books").setSpanKind(SpanKind.SERVER).startSpan()
                try {
                    withContext(Dispatchers.IO) { call.respond(bookService.getAllBooks()) }
                    span.setStatus(StatusCode.OK)
                } catch (e: Exception) {
                    span.setStatus(StatusCode.ERROR, e.message ?: "Unknown error")
                    withContext(Dispatchers.IO) { call.respond(HttpStatusCode.InternalServerError) }
                } finally {
                    span.end()
                }
            }

            post {
                val span =
                        tracer.spanBuilder("create-book").setSpanKind(SpanKind.SERVER).startSpan()
                try {
                    withContext(Dispatchers.IO) {
                        val book = call.receive<Book>()
                        val createdBook = bookService.createBook(book)
                        call.respond(HttpStatusCode.Created, createdBook)
                    }
                    span.setStatus(StatusCode.OK)
                } catch (e: Exception) {
                    span.setStatus(StatusCode.ERROR, e.message ?: "Unknown error")
                    withContext(Dispatchers.IO) {
                        call.respond(
                                HttpStatusCode.BadRequest,
                                mapOf("error" to (e.message ?: "Failed to create book"))
                        )
                    }
                } finally {
                    span.end()
                }
            }
        }
    }
}
