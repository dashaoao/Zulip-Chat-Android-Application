package com.example.test.data.stub

import com.example.chat.domain.EmojiNCS
import com.example.chat.domain.Message
import com.example.chat.domain.MessageRepository
import com.example.common.core.TopicId
import com.example.common.core.result_state.ResultFlow
import com.example.common.core.result_state.success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MessageRepositoryStub : MessageRepository {

    override fun addReaction(idMessage: String, emoji: EmojiNCS): ResultFlow<Unit> {
        return flowOf(Unit.success())
    }

    override fun deleteReaction(idMessage: String, emoji: EmojiNCS): ResultFlow<Unit> {
        return flowOf(Unit.success())
    }

    override fun getMessages(topicId: TopicId, pageSize: Int): ResultFlow<List<Message>> {
        TODO("Not yet implemented")
    }

    override fun getMessageById(idMessage: String): ResultFlow<Message> {
        TODO("Not yet implemented")
    }

    override fun requestMore(topicId: TopicId, pageSize: Int): Flow<List<Message>> {
        TODO("Not yet implemented")
    }

    override fun getEmojiList(): List<EmojiNCS> {
        TODO("Not yet implemented")
    }

    override fun deleteMessage(messageId: Int): ResultFlow<Unit> {
        TODO("Not yet implemented")
    }

    override fun editMessage(messageId: Int, newContent: String): ResultFlow<Unit> {
        TODO("Not yet implemented")
    }

    override fun changeMessageTopic(messageId: Int, newTopic: String): ResultFlow<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getTopicUnreadMessageCount(topicId: TopicId): Int {
        TODO("Not yet implemented")
    }

    override fun sendMessage(topicId: TopicId, text: String): ResultFlow<Unit> {
        return flowOf(Unit.success())
    }
}
