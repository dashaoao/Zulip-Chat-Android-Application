package com.example.channels.domain

import com.example.common.core.TopicId

data class Topic(
    val id: TopicId,
    val title :String,
    val messageCount: Int? = null,
)
