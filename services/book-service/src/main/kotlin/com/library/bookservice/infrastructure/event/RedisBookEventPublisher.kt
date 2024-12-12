package com.library.bookservice.infrastructure.event

import com.library.bookservice.domain.event.BookEventPublisher
import com.library.bookservice.domain.event.EventBus
import com.library.bookservice.domain.model.Book
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RedisBookEventPublisher(private val eventBus: EventBus) : BookEventPublisher {
    companion object {
        const val BOOK_CREATED_CHANNEL = "book.created"
        const val BOOK_UPDATED_CHANNEL = "book.updated"
    }

    override fun publishBookCreated(book: Book) {
        eventBus.publish(BOOK_CREATED_CHANNEL, Json.encodeToString(book))
    }

    override fun publishBookUpdated(book: Book) {
        eventBus.publish(BOOK_UPDATED_CHANNEL, Json.encodeToString(book))
    }

    override fun subscribeToBookEvents() {
        eventBus.subscribe(BOOK_CREATED_CHANNEL) { message ->
            println("Book created event received: $message")
        }

        eventBus.subscribe(BOOK_UPDATED_CHANNEL) { message ->
            println("Book updated event received: $message")
        }
    }
}
