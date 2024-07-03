package com.example.test.data

import com.example.chat.data.MessageRepositoryImpl
import com.example.chat.data.api.ChatApi
import com.example.chat.data.api.events.MessageEventHandler
import com.example.chat.data.api.events.MessageEventHandlerImpl
import com.example.chat.data.api.model.MessageDto
import com.example.chat.data.api.model.MessagesResponse
import com.example.chat.data.api.model.ReactionDto
import com.example.chat.data.api.model.toDomain
import com.example.chat.data.database.ChatDao
import com.example.chat.data.database.model.MessageEntity
import com.example.chat.data.database.model.MessageWithReactions
import com.example.chat.data.database.model.toDomain
import com.example.common.core.TopicId
import com.example.common.core.result_state.ResultState
import com.example.test.data.stub.ChatApiStub
import com.example.test.data.stub.ChatDaoStub
import com.example.test.data.stub.EventsApiStub
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.time.LocalDateTime

@RunWith(JUnit4::class)
class MessageRepositoryImplTest {

    @Test
    fun `getting a list of messages if there is data in database`() = runTest {
        val cache = listOf(
            MessageWithReactions(
                createMessageEntity(1),
                emptyList()
            ),
            MessageWithReactions(
                createMessageEntity(2),
                emptyList()
            ),
            MessageWithReactions(
                createMessageEntity(3),
                emptyList()
            )
        )
        val chatDao = ChatDaoStub().apply { resultProvider = { cache } }
        val repository = createRepository(chatDao = chatDao)

        val messages =
            repository.getMessages(TopicId("1", "Topic"), 50)
                .filter { it !is ResultState.Loading }
                .first()

        assertEquals(messages.data, cache.map { it.toDomain() })
    }

    @Test
    fun `getting a list of messages if there is no data in database`() = runTest {
        val fresh = listOf(
            createMessageDto(1),
            createMessageDto(2),
            createMessageDto(3),
        )
        val messageApi = ChatApiStub().apply { resultProvider = {
            MessagesResponse(
                fresh
            )
        } }
        val repository = createRepository(messageApi = messageApi)

        val messages =
            repository.getMessages(TopicId("1", "Topic"), 50)
                .filter { it !is ResultState.Loading }
                .first()

        assertEquals(messages.data, fresh.map { it.toDomain() })
    }

    @Test
    fun `error loading data from the server`() = runTest {
        val cache = listOf(
            MessageWithReactions(
                createMessageEntity(1),
                emptyList()
            ),
            MessageWithReactions(
                createMessageEntity(2),
                emptyList()
            ),
            MessageWithReactions(
                createMessageEntity(3),
                emptyList()
            )
        )
        val chatDao = ChatDaoStub().apply { resultProvider = { cache } }
        val messageApi = ChatApiStub().apply { resultProvider = { throw Exception() } }
        val repository = createRepository(chatDao, messageApi)

        val messages =
            repository.getMessages(TopicId("1", "Topic"), 50)
                .filter { it !is ResultState.Loading }
                .first()

        assertEquals(messages.data, cache.map { it.toDomain() })
    }

    private fun createRepository(
        chatDao: ChatDao = ChatDaoStub(),
        messageApi: ChatApi = ChatApiStub(),
        messageEventHandler: MessageEventHandler = MessageEventHandlerImpl(
            EventsApiStub()
        )
    ): MessageRepositoryImpl = MessageRepositoryImpl(
        chatDao = chatDao,
        chatApi = messageApi,
        messageEventHandler = messageEventHandler
    )

    private fun createMessageEntity(
        id: Int,
        topicId: TopicId = TopicId(
            "0",
            ""
        ),
        senderId: String = "0",
        senderFullName: String = "",
        text: String = "cached message",
        avatarUrl: String = "",
        dateTime: LocalDateTime = LocalDateTime.now()
    ): MessageEntity =
        MessageEntity(
            id = id,
            topicId = topicId,
            senderId = senderId,
            senderFullName = senderFullName,
            text = text,
            avatarUrl = avatarUrl,
            dateTime = dateTime
        )

    private fun createMessageDto(
        id: Int,
        senderId: Int = 0,
        senderFullName: String = "",
        content: String = "fresh message",
        avatarUrl: String = "",
        timestamp: Long = 0L,
        reactions: List<ReactionDto> = emptyList()
    ): MessageDto = MessageDto(
        id = id,
        senderId = senderId,
        senderFullName = senderFullName,
        content = content,
        avatarUrl = avatarUrl,
        timestamp = timestamp,
        reactions = reactions
    )
}
