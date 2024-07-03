package com.example.chat.data.api.events

import com.example.chat.domain.MessageEvent
import com.example.common.core.TopicId
import kotlinx.coroutines.flow.Flow

interface MessageEventHandler {
    fun getEvents(topicId: TopicId): Flow<List<MessageEvent>>
}
