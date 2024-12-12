package com.library.bookservice.domain.repository

import com.library.bookservice.domain.model.Book

interface BookRepository {
    fun getAllBooks(): List<Book>
    fun getBookById(id: Int): Book?
    fun createBook(book: Book): Book
    fun updateBook(book: Book): Book
}
