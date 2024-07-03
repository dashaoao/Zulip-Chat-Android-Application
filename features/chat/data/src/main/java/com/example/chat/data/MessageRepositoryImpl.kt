package com.example.chat.data

import com.example.chat.data.api.ChatApi
import com.example.chat.data.api.events.MessageEventHandler
import com.example.chat.data.api.model.toDomain
import com.example.chat.data.database.ChatDao
import com.example.chat.data.database.model.toDomain
import com.example.chat.data.database.model.toEntity
import com.example.chat.domain.EmojiNCS
import com.example.chat.domain.Message
import com.example.chat.domain.MessageEventType
import com.example.chat.domain.MessageRepository
import com.example.common.core.TopicId
import com.example.common.core.result_state.ResultFlow
import com.example.common.core.result_state.ResultState
import com.example.common.core.result_state.flowState
import com.example.common.core.result_state.runCatchingState
import com.example.common.core.result_state.success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val chatApi: ChatApi,
    private val messageEventHandler: MessageEventHandler,
    private val chatDao: ChatDao,
) : MessageRepository {

    private val sessionCache = mutableListOf<Message>()
    private var requestInProgress = false
    private var nextId: Int? = null

    companion object {
        private const val NEWEST_NUM_BEFORE = "0"
        private const val NEWEST_NUM_AFTER = "0"
        private const val NEWEST_ANCHOR = "newest"
        private const val FIRST_UNREAD_ANCHOR = "first_unread"
        private const val CACHE_SIZE = 50
    }

    private fun buildSendParams(topicId: TopicId, message: String) = mapOf(
        "type" to "stream",
        "to" to topicId.streamId,
        "topic" to topicId.topicName,
        "content" to message,
    )

    private fun buildGetParams(topicId: TopicId, anchor: String, numBefore: Int) = mapOf(
        "num_before" to numBefore.toString(),
        "num_after" to NEWEST_NUM_AFTER,
        "anchor" to anchor,
        "narrow" to """[{"operand": ${topicId.streamId}, "operator": "stream"},{"operand": "${topicId.topicName}", "operator": "topic"}]"""
    )

    private fun buildUnreadMessageCountParams(topicId: TopicId) = mapOf(
        "num_before" to NEWEST_NUM_BEFORE,
        "num_after" to "5000",
        "anchor" to FIRST_UNREAD_ANCHOR,
        "narrow" to """[{"operand": ${topicId.streamId}, "operator": "stream"},{"operand": "${topicId.topicName}", "operator": "topic"}]"""
    )

    private fun buildReactionParams(emojiName: String) = mapOf(
        "emoji_name" to emojiName,
    )

    override fun sendMessage(topicId: TopicId, text: String): ResultFlow<Unit> =
        flowState {
            chatApi.sendMessage(buildSendParams(topicId, text))
        }

    override fun addReaction(idMessage: String, emoji: EmojiNCS): ResultFlow<Unit> =
        flowState {
            chatApi.addReaction(idMessage.toInt(), buildReactionParams(emoji.name))
        }

    override fun deleteReaction(idMessage: String, emoji: EmojiNCS): ResultFlow<Unit> =
        flowState {
            chatApi.removeReaction(idMessage.toInt(), emoji.name)
        }

    override fun getMessages(topicId: TopicId, pageSize: Int): ResultFlow<List<Message>> = flow {
        runCatchingState {
            requestInProgress = true
            emit(ResultState.Loading())

            getCacheMessages(topicId).let {
                if (it.isNotEmpty()) emit(it.success())
            }

            getFreshMessages(topicId, NEWEST_ANCHOR, pageSize).let {
                sessionCache.apply {
                    clear()
                    addAll(it)
                    emit(this.success())
                }
                requestInProgress = false
                refreshMessages(it, topicId)
            }

            messageEventHandler.getEvents(topicId).collect { events ->
                reloadMessages(
                    topicId = topicId,
                    newMessagesNumber = events.count { it.type == MessageEventType.MESSAGE },
                )
            }
        }
    }

    private suspend fun getCacheMessages(topicId: TopicId): List<Message> =
        chatDao.getMessages(topicId).map { it.toDomain() }

    private suspend fun getFreshMessages(
        topicId: TopicId,
        anchor: String,
        numBefore: Int,
    ): List<Message> =
        chatApi.getMessages(
            buildGetParams(topicId, anchor, numBefore)
        ).messages
            .map { it.toDomain() }
            .also { if (it.isNotEmpty()) nextId = it.first().id.toInt() - 1 }

    private suspend fun refreshMessages(freshMessages: List<Message>, topicId: TopicId) =
        freshMessages.take(CACHE_SIZE).let { messages ->
            chatDao.updateMessages(
                messages = messages.map {
                    it.toEntity(topicId)
                },
                reactions = messages.flatMap { msg -> msg.reactions.map { it.toEntity(msg.id.toInt()) } }
            )
        }

    private suspend fun FlowCollector<ResultState<List<Message>>>.reloadMessages(
        topicId: TopicId,
        newMessagesNumber: Int,
    ) {
        val new = getFreshMessages(topicId, NEWEST_ANCHOR, sessionCache.size + newMessagesNumber)
        sessionCache.apply {
            clear()
            addAll(new)
            emit(this.toList().success())
        }
        refreshMessages(new, topicId)
    }

    override fun requestMore(topicId: TopicId, pageSize: Int): Flow<List<Message>> = flow {
        if (!requestInProgress) {
            requestInProgress = true
            sessionCache.addAll(
                0,
                getFreshMessages(topicId, anchor = nextId.toString(), pageSize)
            )
            emit(sessionCache)
            requestInProgress = false
        }
    }

    override fun getMessageById(idMessage: String): ResultFlow<Message> =
        flowState {
            chatApi.fetchMessage(
                idMessage.toInt()
            ).message.toDomain()
        }

    override fun getEmojiList(): List<EmojiNCS> = emojiList

    override fun deleteMessage(messageId: Int): ResultFlow<Unit> = flowState {
        chatApi.deleteMessage(messageId)
    }

    override fun editMessage(messageId: Int, newContent: String): ResultFlow<Unit> = flowState {
        chatApi.editMessage(messageId, newContent)
    }

    override fun changeMessageTopic(messageId: Int, newTopic: String): ResultFlow<Unit> =
        flowState {
            chatApi.changeMessageTopic(
                messageId = messageId,
                newTopic = newTopic
            )
        }

    override suspend fun getTopicUnreadMessageCount(topicId: TopicId): Int =
        chatApi.getMessages(buildUnreadMessageCountParams(topicId)).messages.size
}
