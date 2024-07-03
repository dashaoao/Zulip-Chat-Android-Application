package com.example.chat.presentation.channel_topics.model

import com.example.channels.presentation.model.ChannelsItem

data class ChannelTopicsUiState(
    val topics: List<ChannelsItem.Topic> = emptyList()
)
