package com.example.chat.presentation.model

import com.example.chat.presentation.ui.custom_message.model.EmojiNCSUi
import com.example.chat.presentation.ui.custom_message.model.ReactionUi
import com.example.common.ui.TopicId
import com.example.common.ui.UiText

data class ChatUiState(
    val chatItems: List<ChatItem> = emptyList(),
    val message: String = "",
    val buttonState: ButtonState = ButtonState.Attach,
    val dialogState: DialogState = DialogState.NoDialog,
    val isLoading: Boolean = false,
    val editingMessage: EditingMessage? = null,
)

data class EditingMessage(
    val id: String,
    val initialText: String,
)

sealed interface DialogState {
    data object NoDialog : DialogState
    class EmojiPick(val idMessage: String, val emojiList: List<EmojiNCSUi>) : DialogState
    class OwnActionBar(val idMessage: String, val emojiList: List<EmojiNCSUi>) : DialogState
    class ActionBar(val idMessage: String, val emojiList: List<EmojiNCSUi>) : DialogState
    class TopicBar(val idMessage: String, val topicId: TopicId) : DialogState
}

sealed interface ButtonState {
    data object Send : ButtonState
    data object Attach : ButtonState
    data object Done : ButtonState
}

sealed interface ChatEvent {
    sealed interface UI : ChatEvent {
        data object Initialize : UI
        class Scroll(val firstVisibleItemPosition: Int) : ChatEvent
        data object OnArrowBackClick : UI
        class OnMessageChange(val text: String) : UI
        class OnLongClick(val idMessage: String, val isOwnMessage: Boolean) : UI
        class OnAddReactionClick(val idMessage: String) : UI
        data object OnChangeTopicClick : UI
        class OnTopicSelected(val idTopic: TopicId) : UI
        data object OnDismissRequest : UI
        class OnEmojiClick(val emoji: EmojiNCSUi) : UI
        class OnReactionClick(val idMessage: String, val reactionUI: ReactionUi) : UI
        data object OnCopyButtonClick : UI
        data object OnDeleteButtonClick : UI
        data object OnEditButtonClick : UI
        data object OnButtonClick : UI
    }

    sealed interface Internal : ChatEvent {
        data object Loading : Internal
        class ChatLoaded(val data: List<ChatItem>) : Internal
        data object TextCopied : Internal
        data object MessageDeleted : Internal
        data object TopicChanged : Internal
        data object MessageEdited : Internal
        class ActionError(val msg: String) : Internal
        class MessageLoaded(val newMessage: String) : Internal
        class ReactionDiaogIsReady(val idMessage: String, val data: List<EmojiNCSUi>) : Internal
        class OwnActionBarIsReady(val idMessage: String, val data: List<EmojiNCSUi>) : Internal
        class ActionBarIsReady(val idMessage: String, val data: List<EmojiNCSUi>) : Internal
        class TopicBarIsReady(val idMessage: String, val topicId: TopicId) : Internal
        class ErrorLoading(val msg: UiText) : Internal
    }
}

sealed class ChatEffect {
    data class Error(val msg: UiText) : ChatEffect()
    data class Copied(val msg: UiText) : ChatEffect()
}

sealed class ChatCommand {
    data object GetMessages : ChatCommand()
    data object LoadMore : ChatCommand()
    data class OnCopyButtonClick(val idMessage: String) : ChatCommand()
    data class OnDeleteButtonClick(val idMessage: String) : ChatCommand()
    data class OnEditButtonClick(val idMessage: String) : ChatCommand()
    data class OnDoneButtonClick(val idMessage: String, val message: String) : ChatCommand()
    data object OnArrowBackClick : ChatCommand()
    data class PrepareReactionDialog(val idMessage: String) : ChatCommand()
    data class PrepareOwnMessageActionBar(val idMessage: String) : ChatCommand()
    data class PrepareMessageActionBar(val idMessage: String) : ChatCommand()
    data class OnEmojiClick(val emoji: EmojiNCSUi, val idMessage: String) : ChatCommand()
    data class OnReactionClick(val idMessage: String, val reactionUI: ReactionUi) : ChatCommand()
    data class OnSendButtonClick(val text: String) : ChatCommand()
    data class OnTopicSelected(val idMessage: String, val topicId: TopicId) : ChatCommand()
    data class OnChangeTopicButtonClick(val idMessage: String) : ChatCommand()
}
