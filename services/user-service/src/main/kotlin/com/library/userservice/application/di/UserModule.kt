package com.library.userservice.application.di

import com.library.userservice.domain.event.EventBus
import com.library.userservice.domain.event.UserEventPublisher
import com.library.userservice.domain.repository.UserRepository
import com.library.userservice.domain.service.UserService
import com.library.userservice.infrastructure.event.RedisEventBus
import com.library.userservice.infrastructure.event.RedisUserEventPublisher
import com.library.userservice.infrastructure.persistence.UserRepositoryImpl
import org.koin.dsl.module

val userModule = module {
    single<EventBus> {
        RedisEventBus(
                host = System.getenv("REDIS_HOST") ?: "localhost",
                port = System.getenv("REDIS_PORT")?.toInt() ?: 6379
        )
    }
    single<UserEventPublisher> { RedisUserEventPublisher(get()) }
    single<UserRepository> { UserRepositoryImpl() }
    single { UserService(get()) }
}
