package com.library.bookservice.infrastructure.persistence

import com.library.bookservice.domain.model.Book
import com.library.bookservice.domain.repository.BookRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class BookRepositoryImpl : BookRepository {
    override fun getAllBooks(): List<Book> = transaction {
        Books.selectAll().map { row ->
            Book(
                    id = row[Books.id],
                    title = row[Books.title],
                    author = row[Books.author],
                    isbn = row[Books.isbn]
            )
        }
    }

    override fun getBookById(id: Int): Book? = transaction {
        Books.select { Books.id eq id }
                .map { row ->
                    Book(
                            id = row[Books.id],
                            title = row[Books.title],
                            author = row[Books.author],
                            isbn = row[Books.isbn]
                    )
                }
                .singleOrNull()
    }

    override fun createBook(book: Book): Book = transaction {
        val id =
                Books.insert {
                    it[title] = book.title
                    it[author] = book.author
                    it[isbn] = book.isbn
                } get Books.id

        book.copy(id = id)
    }

    override fun updateBook(book: Book): Book = transaction {
        Books.update({ Books.id eq book.id }) {
            it[title] = book.title
            it[author] = book.author
            it[isbn] = book.isbn
        }
        book
    }
}
