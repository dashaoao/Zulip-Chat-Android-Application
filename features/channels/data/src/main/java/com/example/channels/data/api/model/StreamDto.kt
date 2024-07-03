package com.example.channels.data.api.model

import com.example.channels.data.database.model.ChannelEntity
import com.example.channels.data.database.model.SubscribedChannelEntity
import com.example.channels.domain.Channel
import com.google.gson.annotations.SerializedName

class StreamDto (
    @SerializedName("stream_id") val id: Int,
    val name: String,
)

fun StreamDto.toDomain() = Channel(
    id = id.toString(),
    title = name,
    topics = emptyList()
)

fun StreamDto.toChannelEntity() = ChannelEntity(
    id = id,
    name = name,
)

fun StreamDto.toSubscribedChannelEntity() = SubscribedChannelEntity(
    id = id,
    name = name,
)
