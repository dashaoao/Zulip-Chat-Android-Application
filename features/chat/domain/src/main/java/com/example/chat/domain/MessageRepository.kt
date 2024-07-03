package com.example.chat.domain

import com.example.common.core.TopicId
import com.example.common.core.result_state.ResultFlow
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun sendMessage(topicId: TopicId, text: String): ResultFlow<Unit>

    fun addReaction(idMessage: String, emoji: EmojiNCS): ResultFlow<Unit>

    fun deleteReaction(idMessage: String, emoji: EmojiNCS): ResultFlow<Unit>

    fun getMessages(topicId: TopicId, pageSize: Int): ResultFlow<List<Message>>

    fun getMessageById(idMessage: String): ResultFlow<Message>

    fun requestMore(topicId: TopicId, pageSize: Int): Flow<List<Message>>

    fun getEmojiList(): List<EmojiNCS>

    fun deleteMessage(messageId: Int): ResultFlow<Unit>

    fun editMessage(messageId: Int, newContent: String): ResultFlow<Unit>

    fun changeMessageTopic(messageId: Int, newTopic: String) : ResultFlow<Unit>

    suspend fun getTopicUnreadMessageCount(topicId: TopicId): Int
}
