package com.library.bookservice.domain.event

import com.library.bookservice.domain.model.Book

interface BookEventPublisher {
    fun publishBookCreated(book: Book)
    fun publishBookUpdated(book: Book)
    fun subscribeToBookEvents()
}
