package com.example.chat.data.api.model

import com.example.chat.domain.EmojiNCS
import com.example.chat.domain.Reaction
import com.google.gson.annotations.SerializedName

class ReactionDto (
    @SerializedName("emoji_name") val emojiName : String,
    @SerializedName("emoji_code") val emojiCode : String,
    @SerializedName("user_id") val userId: String,
)

fun ReactionDto.toDomain() = Reaction(
    userId = userId,
    emoji = EmojiNCS(
        name = emojiName,
        code = emojiCode
    )
)
