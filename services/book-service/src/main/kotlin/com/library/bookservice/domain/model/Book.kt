package com.library.bookservice.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Book(val id: Int = 0, val title: String, val author: String, val isbn: String)
