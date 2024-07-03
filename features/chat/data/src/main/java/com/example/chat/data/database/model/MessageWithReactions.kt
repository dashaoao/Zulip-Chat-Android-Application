package com.example.chat.data.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.chat.domain.Message

data class MessageWithReactions(
    @Embedded val message: MessageEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "message_id"
    )
    val reactions: List<ReactionEntity>
)

fun MessageWithReactions.toDomain(): Message =
    Message(
        id = message.id.toString(),
        userId = message.senderId,
        name = message.senderFullName,
        message = message.text,
        icon = message.avatarUrl,
        dateTime = message.dateTime,
        reactions = reactions.map { it.toDomain() },
    )
