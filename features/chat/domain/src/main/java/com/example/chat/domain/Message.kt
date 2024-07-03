package com.example.chat.domain

import java.time.LocalDateTime

data class Message(
    val id: String,
    val userId: String,
    val icon: String?,
    val name: String,
    val message: String,
    val dateTime: LocalDateTime,
    val reactions: List<Reaction>
)
