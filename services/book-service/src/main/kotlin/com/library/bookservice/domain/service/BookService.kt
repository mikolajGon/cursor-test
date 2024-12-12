package com.library.bookservice.domain.service

import com.library.bookservice.domain.event.BookEventPublisher
import com.library.bookservice.domain.model.Book
import com.library.bookservice.domain.repository.BookRepository

class BookService(
        private val repository: BookRepository,
        private val eventPublisher: BookEventPublisher
) {
    init {
        eventPublisher.subscribeToBookEvents()
    }

    fun getAllBooks(): List<Book> = repository.getAllBooks()

    fun createBook(book: Book): Book {
        val createdBook = repository.createBook(book)
        eventPublisher.publishBookCreated(createdBook)
        return createdBook
    }

    fun updateBook(book: Book): Book {
        val updatedBook = repository.updateBook(book)
        eventPublisher.publishBookUpdated(updatedBook)
        return updatedBook
    }
}
