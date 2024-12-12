package com.library.userservice.domain.event

import com.library.userservice.domain.model.User

interface UserEventPublisher {
    fun publishUserCreated(user: User)
    fun publishUserUpdated(user: User)
    fun subscribeToUserEvents()
}
