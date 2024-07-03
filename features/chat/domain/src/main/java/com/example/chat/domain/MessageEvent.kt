package com.example.chat.domain

data class MessageEvent(
    val id: Int,
    val type: MessageEventType,
)
