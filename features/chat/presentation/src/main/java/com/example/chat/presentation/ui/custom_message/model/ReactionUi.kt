package com.example.chat.presentation.ui.custom_message.model

import com.example.chat.domain.Reaction

data class ReactionUi(
    val emoji: EmojiNCSUi,
    val count: Int,
    val selected: Boolean
)

fun List<Reaction>.toUi(userId: String) = groupBy { it.emoji }.map {
    ReactionUi(
        emoji = it.key.toUi(),
        count = it.value.size,
        selected = it.value.any { reaction -> reaction.userId == userId }
    )
}
