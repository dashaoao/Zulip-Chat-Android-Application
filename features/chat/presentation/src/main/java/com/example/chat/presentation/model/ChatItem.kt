package com.example.chat.presentation.model

import com.example.chat.domain.Message
import com.example.chat.presentation.ui.custom_message.model.ReactionUi
import com.example.chat.presentation.ui.custom_message.model.toUi
import java.time.format.DateTimeFormatter
import java.util.Locale

sealed interface ChatItem {
    val id: String

    class Date(
        val date: String
    ) : ChatItem {
        override val id: String = date
    }

    data class Message(
        override val id: String,
        val icon: String?,
        val name: String,
        val message: String,
        val reactions: List<ReactionUi>,
    ) : ChatItem

    class OwnMessage(
        override val id: String,
        val message: String,
        val reactions: List<ReactionUi>,
    ) : ChatItem
}

fun List<Message>.toChatItemList(userId: String) = groupBy { it.dateTime.toLocalDate() }.flatMap {
    val dateItem =
        ChatItem.Date(date = it.key.format(DateTimeFormatter.ofPattern("dd MMMM", Locale.ENGLISH)))
    val messageItems = it.value.map { msg -> msg.toUi(userId) }
    listOf(dateItem) + messageItems
}.reversed()

fun Message.toUi(userId: String) = if (this.userId == userId) {
    ChatItem.OwnMessage(
        id = id,
        message = message,
        reactions = reactions.toUi(userId)
    )
} else {
    ChatItem.Message(
        id = id,
        icon = icon,
        name = name,
        message = message,
        reactions = reactions.toUi(userId)
    )
}
