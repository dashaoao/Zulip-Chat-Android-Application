package com.example.test.presentation.data

import android.app.Application
import com.example.chat.domain.MessageRepository
import com.example.chat.presentation.ChatActor
import com.example.chat.presentation.ChatNavigation
import com.example.chat.presentation.ui.custom_message.model.EmojiNCSUi
import com.example.chat.presentation.ui.custom_message.model.ReactionUi
import com.example.common.ui.TopicId
import com.example.users.domain.UsersRepository
import org.mockito.Mockito.mock

object ChatActorTestData {
    const val ID_MESSAGE = "testIdMessage"
    const val TEXT = "testText"
    val EMOJI_LIST = listOf<EmojiNCSUi>()
    private const val EMOJI_NAME = "grinning"
    private const val EMOJI_UNICODE = "1f600"
    private const val COUNT = 1
    private const val SELECTED = true
    val REACTION_UI = ReactionUi(
        EmojiNCSUi(
            EMOJI_NAME,
            EMOJI_UNICODE
        ), COUNT, SELECTED
    )
    private const val STREAM_ID = "1"
    private const val TOPIC_NAME = "testTopicName"
    val TOPIC_ID = TopicId(STREAM_ID, TOPIC_NAME)
    val MESSAGE_REPOSITORY: MessageRepository = mock(MessageRepository::class.java)
    private val USERS_REPOSITORY: UsersRepository = mock(UsersRepository::class.java)
    private val ROUTER: ChatNavigation = mock(ChatNavigation::class.java)
    private val APPLICATION: Application = mock(Application::class.java)
    val CHAT_ACTOR = ChatActor(
        MESSAGE_REPOSITORY,
        USERS_REPOSITORY,
        TOPIC_ID,
        ROUTER,
        APPLICATION
    )
}
