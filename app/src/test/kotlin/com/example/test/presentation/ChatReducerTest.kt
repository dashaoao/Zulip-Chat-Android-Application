package com.example.test.presentation

import com.example.chat.presentation.ChatReducer
import com.example.chat.presentation.model.ButtonState
import com.example.chat.presentation.model.ChatCommand
import com.example.chat.presentation.model.ChatEffect
import com.example.chat.presentation.model.ChatEvent
import com.example.chat.presentation.model.DialogState
import com.example.chat.presentation.ui.custom_message.model.EmojiNCSUi
import com.example.chat.presentation.ui.custom_message.model.ReactionUi
import com.example.common.ui.UiText
import com.example.test.presentation.data.ChatReducerTestData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ChatReducerTest {

    private val testData = ChatReducerTestData()

    @Test
    fun `reduce WHEN event Initialize THEN state isLoading should be true and command should be GetMessages`() {
        // Given
        val reducer = ChatReducer()

        // When
        val actual = reducer.reduce(ChatEvent.UI.Initialize, testData.initialState)

        // Then
        assertTrue(actual.state.isLoading)
        assertEquals(listOf(ChatCommand.GetMessages), actual.commands)
    }

    @Test
    fun `reduce WHEN event ChatLoaded THEN state isLoading should be false and chatItems should be updated`() {
        // Given
        val reducer = ChatReducer()

        // When
        val actual =
            reducer.reduce(ChatEvent.Internal.ChatLoaded(testData.chatItems), testData.initialState)

        // Then
        assertFalse(actual.state.isLoading)
        assertEquals(testData.chatItems, actual.state.chatItems)
    }

    @Test
    fun `reduce WHEN event ErrorLoading THEN effect should be Error with the correct message`() {
        // Given
        val reducer = ChatReducer()

        // When
        val actual = reducer.reduce(
            ChatEvent.Internal.ErrorLoading(UiText.RawString(testData.errorMessage)),
            testData.initialState
        )

        // Then
        assertEquals(
            listOf(ChatEffect.Error(UiText.RawString(testData.errorMessage))),
            actual.effects
        )
    }

    @Test
    fun `reduce WHEN event Loading THEN state isLoading should be true`() {
        // Given
        val reducer = ChatReducer()

        // When
        val actual = reducer.reduce(ChatEvent.Internal.Loading, testData.initialState)

        // Then
        assertTrue(actual.state.isLoading)
    }

    @Test
    fun `reduce WHEN event OnArrowBackClick THEN commands should contain OnArrowBackClick`() {
        // Given
        val reducer = ChatReducer()

        // When
        val actual = reducer.reduce(ChatEvent.UI.OnArrowBackClick, testData.initialState)

        // Then
        assertTrue(actual.commands.contains(ChatCommand.OnArrowBackClick))
    }

    @Test
    fun `reduce WHEN event OnMessageChange THEN state message and buttonState should be updated`() {
        // Given
        val reducer = ChatReducer()

        // When
        val actual =
            reducer.reduce(ChatEvent.UI.OnMessageChange(testData.newMessage), testData.initialState)

        // Then
        assertEquals(testData.newMessage, actual.state.message)
        assertEquals(ButtonState.Send, actual.state.buttonState)
    }

    @Test
    fun `reduce WHEN event OnReactionClick THEN commands should contain OnReactionClick`() {
        // Given
        val reducer = ChatReducer()

        // When
        val actual = reducer.reduce(testData.onReactionClickEvent, testData.initialState)
        val expectedCommand = ChatCommand.OnReactionClick(
            "1",
            ReactionUi(
                EmojiNCSUi(
                    "grinning",
                    "1f600"
                ),
                1,
                true
            )
        )

        // Then
        assertTrue(actual.commands.contains(expectedCommand))
    }

    @Test
    fun `reduce WHEN event OnButtonClick and buttonState is Attach THEN state should remain the same`() {
        // Given
        val reducer = ChatReducer()

        // When
        val actual =
            reducer.reduce(ChatEvent.UI.OnButtonClick, testData.initialStateWithButtonStateAttach)

        // Then
        assertEquals(testData.initialStateWithButtonStateAttach, actual.state)
    }

    @Test
    fun `reduce WHEN event OnButtonClick and buttonState is Send THEN state message should be empty and buttonState should be Attach`() {
        // Given
        val reducer = ChatReducer()

        // When
        val actual =
            reducer.reduce(ChatEvent.UI.OnButtonClick, testData.initialStateWithButtonStateSend)

        // Then
        assertEquals("", actual.state.message)
        assertEquals(ButtonState.Attach, actual.state.buttonState)
        assertTrue(actual.commands.contains(ChatCommand.OnSendButtonClick(testData.initialStateWithButtonStateSend.message)))
    }

    @Test
    fun `reduce WHEN event OnDismissRequest THEN state dialogState should be NoDialog`() {
        // Given
        val reducer = ChatReducer()

        // When
        val actual = reducer.reduce(
            ChatEvent.UI.OnDismissRequest,
            testData.initialStateWithDialogStateEmojiPick
        )

        // Then
        assertEquals(DialogState.NoDialog, actual.state.dialogState)
    }

    @Test
    fun `reduce WHEN event OnEmojiClick and dialogState is EmojiPick THEN state dialogState should be NoDialog and commands should contain OnEmojiClick`() {
        // Given
        val reducer = ChatReducer()

        // When
        val actual = reducer.reduce(
            ChatEvent.UI.OnEmojiClick(testData.emoji),
            testData.initialStateWithDialogStateEmojiPick
        )

        // Then
        assertEquals(DialogState.NoDialog, actual.state.dialogState)
        assertTrue(
            actual.commands.contains(
                ChatCommand.OnEmojiClick(
                    testData.emoji,
                    testData.idMessage
                )
            )
        )
    }
}
