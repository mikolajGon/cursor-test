package com.library.userservice.domain.event

interface EventBus {
    fun publish(channel: String, message: String)
    fun subscribe(channel: String, callback: (String) -> Unit)
}
