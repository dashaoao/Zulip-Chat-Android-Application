package com.example.channels.presentation.model

import com.example.channels.domain.Channel
import com.example.channels.domain.Topic
import com.example.common.ui.TopicId
import com.example.common.ui.toUi

sealed interface ChannelsItem {

    val id: String

    data class Channel(
        override val id: String,
        val title: String,
        val expanded: Boolean,
        val isTopicLoading: Boolean = false,
    ) : ChannelsItem

    data class Topic(
        val topicId: TopicId,
        val title: String,
        val messageCount: Int?
    ) : ChannelsItem {
        override val id: String = "${topicId.streamId}-${topicId.topicName}"
    }
}

fun Channel.toUI(expanded: Boolean = false) = ChannelsItem.Channel(
    id = id,
    title = title,
    expanded = expanded
)

fun Topic.toUI() = ChannelsItem.Topic(
    topicId = id.toUi(),
    title = title,
    messageCount = messageCount,
)
