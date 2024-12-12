package com.library.bookservice.application.di

import com.library.bookservice.domain.event.BookEventPublisher
import com.library.bookservice.domain.event.EventBus
import com.library.bookservice.domain.repository.BookRepository
import com.library.bookservice.domain.service.BookService
import com.library.bookservice.infrastructure.event.RedisBookEventPublisher
import com.library.bookservice.infrastructure.event.RedisEventBus
import com.library.bookservice.infrastructure.persistence.BookRepositoryImpl
import org.koin.dsl.module

val bookModule = module {
    single<EventBus> {
        RedisEventBus(
                host = System.getenv("REDIS_HOST") ?: "localhost",
                port = System.getenv("REDIS_PORT")?.toInt() ?: 6379
        )
    }
    single<BookEventPublisher> { RedisBookEventPublisher(get()) }
    single<BookRepository> { BookRepositoryImpl() }
    single { BookService(get(), get()) }
}
