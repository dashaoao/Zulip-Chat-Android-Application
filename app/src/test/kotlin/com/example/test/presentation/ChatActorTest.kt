package com.example.test.presentation

import com.example.chat.presentation.model.ChatCommand
import com.example.chat.presentation.model.ChatEvent
import com.example.chat.presentation.ui.custom_message.model.toDomain
import com.example.common.core.result_state.ResultState
import com.example.common.ui.toDomain
import com.example.test.presentation.data.ChatActorTestData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`

@RunWith(JUnit4::class)
class ChatActorTest {

    @Test
    fun `execute WHEN command GetEmojiList THEN should return EmojiListLoaded event`() = runTest {
        // Given
        `when`(ChatActorTestData.MESSAGE_REPOSITORY.getEmojiList()).thenReturn(ChatActorTestData.EMOJI_LIST.map { it.toDomain() })

        // When
        val actual =
            ChatActorTestData.CHAT_ACTOR.execute(ChatCommand.PrepareReactionDialog(ChatActorTestData.ID_MESSAGE))
                .first()

        // Then
        assertTrue(actual is ChatEvent.Internal.ReactionDiaogIsReady)
        assertEquals(
            ChatActorTestData.ID_MESSAGE,
            (actual as ChatEvent.Internal.ReactionDiaogIsReady).idMessage
        )
        assertEquals(ChatActorTestData.EMOJI_LIST, actual.data)
    }

    @Test
    fun `onReactionClick WHEN reaction selected THEN should return ErrorLoading event`() = runTest {
        // Given
        `when`(
            ChatActorTestData.MESSAGE_REPOSITORY.deleteReaction(
                ChatActorTestData.ID_MESSAGE,
                ChatActorTestData.REACTION_UI.emoji.toDomain()
            )
        ).thenReturn(
            flowOf(ResultState.Error(Exception()))
        )

        // When
        val actual = ChatActorTestData.CHAT_ACTOR.onReactionClick(
            ChatActorTestData.ID_MESSAGE,
            ChatActorTestData.REACTION_UI
        ).first()

        // Then
        assertTrue(actual is ChatEvent.Internal.ErrorLoading)
    }

    @Test
    fun `onButtonClick THEN should return ErrorLoading event`() = runTest {
        // Given
        `when`(
            ChatActorTestData.MESSAGE_REPOSITORY.sendMessage(
                ChatActorTestData.TOPIC_ID.toDomain(),
                ChatActorTestData.TEXT
            )
        ).thenReturn(
            flowOf(ResultState.Error(Exception()))
        )

        // When
        val actual = ChatActorTestData.CHAT_ACTOR.onSendButtonClick(ChatActorTestData.TEXT).first()

        // Then
        assertTrue(actual is ChatEvent.Internal.ErrorLoading)
    }
}
