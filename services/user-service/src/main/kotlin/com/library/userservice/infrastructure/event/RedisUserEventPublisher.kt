package com.library.userservice.infrastructure.event

import com.library.userservice.domain.event.EventBus
import com.library.userservice.domain.event.UserEventPublisher
import com.library.userservice.domain.model.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RedisUserEventPublisher(private val eventBus: EventBus) : UserEventPublisher {
    companion object {
        const val USER_CREATED_CHANNEL = "user.created"
        const val USER_UPDATED_CHANNEL = "user.updated"
    }

    override fun publishUserCreated(user: User) {
        eventBus.publish(USER_CREATED_CHANNEL, Json.encodeToString(user))
    }

    override fun publishUserUpdated(user: User) {
        eventBus.publish(USER_UPDATED_CHANNEL, Json.encodeToString(user))
    }

    override fun subscribeToUserEvents() {
        eventBus.subscribe(USER_CREATED_CHANNEL) { message ->
            println("User created event received: $message")
            // Handle user created event
        }

        eventBus.subscribe(USER_UPDATED_CHANNEL) { message ->
            println("User updated event received: $message")
            // Handle user updated event
        }
    }
}
