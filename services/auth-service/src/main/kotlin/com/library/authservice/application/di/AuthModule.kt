package com.library.authservice.application.di

import com.library.authservice.domain.repository.AuthRepository
import com.library.authservice.domain.service.AuthService
import com.library.authservice.domain.service.JwtService
import com.library.authservice.infrastructure.persistence.AuthRepositoryImpl
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val authModule = module {
    single {
        val mongoUrl = System.getenv("MONGO_URL") ?: "mongodb://authuser:authpass@localhost:27017"
        val client = KMongo.createClient(mongoUrl).coroutine
        client.getDatabase("authdb")
    }
    single { JwtService() }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single { AuthService(get(), get()) }
}
