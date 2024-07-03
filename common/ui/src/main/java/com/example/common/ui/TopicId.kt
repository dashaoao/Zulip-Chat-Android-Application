package com.example.common.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TopicId(
    val streamId: String,
    val topicName: String
) : Parcelable

fun TopicId.toDomain() = com.example.common.core.TopicId(
    streamId = streamId,
    topicName = topicName
)

fun com.example.common.core.TopicId.toUi() = TopicId(
    streamId = streamId,
    topicName = topicName
)
