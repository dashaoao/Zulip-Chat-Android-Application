package com.example.test.presentation.data

import com.example.chat.presentation.model.ButtonState
import com.example.chat.presentation.model.ChatEvent
import com.example.chat.presentation.model.ChatItem
import com.example.chat.presentation.model.ChatUiState
import com.example.chat.presentation.model.DialogState
import com.example.chat.presentation.ui.custom_message.model.EmojiNCSUi
import com.example.chat.presentation.ui.custom_message.model.ReactionUi

class ChatReducerTestData {

    companion object {
        const val DATE_1 = "20 Марта"
        const val DATE_2 = "21 Марта"
        const val ID_1 = "1"
        const val ID_2 = "2"
        const val ID_3 = "3"
        const val ICON_URL = "https://i.pinimg.com/736x/bb/33/2f/bb332fa027610ceb7e5cbb8b39987c55.jpg"
        const val NAME = "Dasha"
        const val MESSAGE_1 = "A bun is rolling. Rolling… Rolling… And then he stops and says, \"I have a headache.\""
        const val MESSAGE_2 = "Hello world!"
        const val MESSAGE_3 = "Shut up"
        const val ERROR_MESSAGE = "Something went wrong"
        const val NEW_MESSAGE = "New message"
        const val EMOJI_NAME = "grinning"
        const val EMOJI_UNICODE = "1f600"
    }

    val initialState = ChatUiState()

    val chatItems = listOf(
        ChatItem.Date(DATE_1),
        ChatItem.Message(
            id = ID_1,
            icon = ICON_URL,
            name = NAME,
            message = MESSAGE_1,
            reactions = mutableListOf(),
        ),
        ChatItem.Date(DATE_2),
        ChatItem.Message(
            id = ID_2,
            icon = ICON_URL,
            name = NAME,
            message = MESSAGE_2,
            reactions = mutableListOf(),
        ),
        ChatItem.OwnMessage(
            id = ID_3,
            message = MESSAGE_3,
            reactions = mutableListOf(),
        ),
    )

    val onReactionClickEvent = ChatEvent.UI.OnReactionClick(
        ID_1,
        ReactionUi(
            EmojiNCSUi(
                EMOJI_NAME,
                EMOJI_UNICODE
            ),
            1,
            true
        ),
    )

    val initialStateWithButtonStateAttach =
        ChatUiState(buttonState = ButtonState.Attach)

    val initialStateWithButtonStateSend = ChatUiState(
        buttonState = ButtonState.Send,
        message = NEW_MESSAGE
    )

    val initialStateWithDialogStateEmojiPick = ChatUiState(
        dialogState = DialogState.EmojiPick(
            ID_1,
            emptyList()
        )
    )

    val emoji = EmojiNCSUi(EMOJI_NAME, EMOJI_UNICODE)

    val idMessage = ID_1

    val errorMessage = ERROR_MESSAGE

    val newMessage = NEW_MESSAGE
}
