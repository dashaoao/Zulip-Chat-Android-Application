package com.example.channels.data.api.model

import com.example.channels.domain.Topic
import com.example.common.core.TopicId
import com.google.gson.annotations.SerializedName

class TopicDto (
    @SerializedName("max_id") val id : Int,
    val name : String,
)

fun TopicDto.toDomain(channelId: String) = Topic(
    id = TopicId(channelId, name),
    title = name,
)
