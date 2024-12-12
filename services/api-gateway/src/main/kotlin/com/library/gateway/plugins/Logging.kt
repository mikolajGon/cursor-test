package com.library.gateway.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

fun Application.configureLogging() {
    val log = LoggerFactory.getLogger("ApiGateway")

    environment.monitor.subscribe(ApplicationStarted) { log.info("API Gateway started") }

    environment.monitor.subscribe(ApplicationStopped) { log.info("API Gateway stopped") }

    install(CallLogging) {
        level = Level.INFO
        disableDefaultColors()

        filter { call -> call.request.path().startsWith("/api") }

        mdc("path") { call -> call.request.path() }

        mdc("method") { call -> call.request.httpMethod.value }
    }
}
