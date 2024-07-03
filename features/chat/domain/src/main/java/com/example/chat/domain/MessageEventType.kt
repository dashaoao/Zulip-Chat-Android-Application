package com.example.chat.domain

enum class MessageEventType {
    MESSAGE,
    OTHER,
    ;
    companion object {
        fun valueOfOrOther(value: String) = when (value) {
            "message" -> MESSAGE
            else -> OTHER
        }
    }
}
