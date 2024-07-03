package com.example.chat.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.chat.domain.EmojiNCS
import com.example.chat.domain.Reaction

@Entity(tableName = "reactions")
class ReactionEntity (
    @PrimaryKey val id: String,
    @ColumnInfo("message_id") val messageId: Int,
    @ColumnInfo("emoji_name") val emojiName : String,
    @ColumnInfo("emoji_code") val emojiCode : String,
    @ColumnInfo("user_id") val userId: String,
)

fun ReactionEntity.toDomain(): Reaction = Reaction(
    emoji = EmojiNCS(emojiName, emojiCode),
    userId = userId
)

fun Reaction.toEntity(messageId: Int) = ReactionEntity(
    id = "$messageId${emoji.code}$userId",
    messageId = messageId,
    emojiName = emoji.name,
    emojiCode = emoji.code,
    userId = userId,
)
