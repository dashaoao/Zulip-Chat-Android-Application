package com.example.chat.presentation.ui.custom_message.model

import com.example.chat.domain.EmojiNCS

data class EmojiNCSUi(
    val name: String,
    val code: String
) {
    fun getCodeString(): String {
        return try {
            String(Character.toChars(code.split('-')[0].toInt(16)))
        } catch (e: NumberFormatException) {
            "?"
        }
    }
}
fun EmojiNCS.toUi() = EmojiNCSUi(
    name = name,
    code = code,
)

fun EmojiNCSUi.toDomain() = EmojiNCS(
    name = name,
    code = code,
)
