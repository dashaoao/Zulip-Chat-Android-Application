package com.example.chat.data.api.model

import com.example.chat.domain.Message
import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.time.LocalDateTime
import java.util.TimeZone

data class MessageDto(
    val id : Int,
    @SerializedName("sender_id") val senderId: Int,
    @SerializedName("sender_full_name") val senderFullName: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    val timestamp: Long,
    val content: String,
    val reactions: List<ReactionDto>,
)

fun MessageDto.toDomain() = Message(
    id = id.toString(),
    userId = senderId.toString(),
    name = senderFullName,
    icon = avatarUrl,
    message = content,
    dateTime = LocalDateTime.ofInstant(
        Instant.ofEpochSecond(timestamp),
        TimeZone.getDefault().toZoneId()
    ),
    reactions = reactions.map { it.toDomain() }
)
