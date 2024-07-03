package com.example.chat.presentation

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.Html
import com.example.chat.domain.MessageRepository
import com.example.chat.presentation.model.ChatCommand
import com.example.chat.presentation.model.ChatEvent
import com.example.chat.presentation.model.toChatItemList
import com.example.chat.presentation.ui.custom_message.model.EmojiNCSUi
import com.example.chat.presentation.ui.custom_message.model.ReactionUi
import com.example.chat.presentation.ui.custom_message.model.toDomain
import com.example.chat.presentation.ui.custom_message.model.toUi
import com.example.common.core.result_state.ResultState
import com.example.common.core.result_state.onError
import com.example.common.core.result_state.onSuccess
import com.example.common.ui.R
import com.example.common.ui.TopicId
import com.example.common.ui.UiText
import com.example.common.ui.toDomain
import com.example.users.domain.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import vivid.money.elmslie.coroutines.Actor

class ChatActor(
    private val messageRepository: MessageRepository,
    private val usersRepository: UsersRepository,
    private val topicId: TopicId,
    private val chatNavigation: ChatNavigation,
    private val application: Application
) : Actor<ChatCommand, ChatEvent> {

    override fun execute(command: ChatCommand): Flow<ChatEvent> {
        return when (command) {
            is ChatCommand.PrepareReactionDialog -> {
                prepareReactionDialog(command.idMessage)
            }

            is ChatCommand.GetMessages -> {
                getMessages(true)
            }

            is ChatCommand.OnArrowBackClick -> {
                onArrowBackClick()
            }

            is ChatCommand.OnEmojiClick -> {
                onEmojiClick(command.emoji, command.idMessage)
            }

            is ChatCommand.OnReactionClick -> {
                onReactionClick(command.idMessage, command.reactionUI)
            }

            is ChatCommand.OnSendButtonClick -> {
                onSendButtonClick(command.text)
            }

            ChatCommand.LoadMore -> {
                loadMore()
            }

            is ChatCommand.OnCopyButtonClick -> {
                onCopyButtonClick(command.idMessage)
            }

            is ChatCommand.OnDeleteButtonClick -> {
                deleteMessage(command.idMessage)
            }

            is ChatCommand.OnEditButtonClick -> {
                onEditButtonClick(command.idMessage)
            }

            is ChatCommand.OnDoneButtonClick -> {
                onDoneButtonClick(command.idMessage, command.message)
            }

            is ChatCommand.OnChangeTopicButtonClick -> {
                onChangeTopicButtonClick(command.idMessage)
            }

            is ChatCommand.OnTopicSelected -> {
                onTopicSelected(command.idMessage, command.topicId)
            }

            is ChatCommand.PrepareMessageActionBar -> {
                prepareMessageActionBar(messageId = command.idMessage, isOwnMessage = false)
            }

            is ChatCommand.PrepareOwnMessageActionBar -> {
                prepareMessageActionBar(messageId = command.idMessage, isOwnMessage = true)
            }
        }
    }

    private fun onArrowBackClick(): Flow<ChatEvent> {
        chatNavigation.exitChat()
        return emptyFlow()
    }

    private fun prepareMessageActionBar(messageId: String, isOwnMessage: Boolean): Flow<ChatEvent.Internal> {
        val reactions = messageRepository.getEmojiList().map { it.toUi() }
        return flowOf(
            if (isOwnMessage) {
                ChatEvent.Internal.OwnActionBarIsReady(messageId, reactions)
            } else {
                ChatEvent.Internal.ActionBarIsReady(messageId, reactions)
            }
        )
    }

    private fun prepareReactionDialog(idMessage: String): Flow<ChatEvent> {
        return flowOf(
            ChatEvent.Internal.ReactionDiaogIsReady(
                idMessage,
                messageRepository.getEmojiList().map { it.toUi() }
            )
        )
    }

    private fun onEmojiClick(emoji: EmojiNCSUi, idMessage: String): Flow<ChatEvent> = flow {
        messageRepository.getMessageById(idMessage).collectLatest { result ->
            result.onSuccess { message ->
                val currentReaction = message.reactions.find { it.emoji.toUi() == emoji }
                if (currentReaction == null) {
                    messageRepository.addReaction(idMessage, emoji.toDomain())
                        .collectLatest { result ->
                            result.onError { _, _ ->
                                emit(ChatEvent.Internal.ErrorLoading(UiText.Resource(R.string.error)))
                            }
                        }
                } else {
                    messageRepository.deleteReaction(idMessage, emoji.toDomain())
                        .collectLatest { result ->
                            result.onError { _, _ ->
                                emit(ChatEvent.Internal.ErrorLoading(UiText.Resource(R.string.error)))
                            }
                        }
                }
            }
        }
    }

    fun onReactionClick(idMessage: String, reactionUI: ReactionUi): Flow<ChatEvent> {
        return if (reactionUI.selected) {
            val deleteReaction = messageRepository.deleteReaction(
                idMessage,
                reactionUI.emoji.toDomain()
            )
            deleteReaction.map { result ->
                when (result) {
                    is ResultState.Error -> {
                        ChatEvent.Internal.ErrorLoading(UiText.Resource(R.string.error))
                    }

                    is ResultState.Loading -> {
                        ChatEvent.Internal.Loading
                    }

                    is ResultState.Success -> {
                        ChatEvent.Internal.Loading
                    }
                }
            }
        } else {
            val addReaction = messageRepository.addReaction(
                idMessage,
                reactionUI.emoji.toDomain()
            )
            addReaction.map { result ->
                when (result) {
                    is ResultState.Error -> {
                        ChatEvent.Internal.ErrorLoading(UiText.Resource(R.string.error))
                    }

                    is ResultState.Loading -> {
                        ChatEvent.Internal.Loading
                    }

                    is ResultState.Success -> {
                        ChatEvent.Internal.Loading
                    }
                }
            }
        }.run {
            this.filter { it is ChatEvent.Internal.ErrorLoading }
        }
    }

    fun onSendButtonClick(text: String): Flow<ChatEvent> {
        return messageRepository.sendMessage(topicId.toDomain(), text).map { result ->
            when (result) {
                is ResultState.Error -> {
                    ChatEvent.Internal.ErrorLoading(UiText.Resource(R.string.error))
                }

                else -> ChatEvent.Internal.Loading
            }
        }.run {
            this.filter { it is ChatEvent.Internal.ErrorLoading }
        }
    }

    private fun onDoneButtonClick(idMessage: String, text: String): Flow<ChatEvent> {
        return messageRepository.editMessage(messageId = idMessage.toInt(), newContent = text)
            .map { result ->
                when (result) {
                    is ResultState.Error -> {
                        ChatEvent.Internal.ErrorLoading(UiText.RawString("Can't edit message"))
                    }

                    else -> ChatEvent.Internal.Loading
                }
            }.run {
                this.filter { it is ChatEvent.Internal.ErrorLoading }
            }
    }

    private fun onCopyButtonClick(idMessage: String): Flow<ChatEvent> =
        channelFlow {
            messageRepository.getMessageById(idMessage).collectLatest { result ->
                result.onSuccess { message ->
                    val formattedMessage = Html.fromHtml(message.message).toString().trim()
                    val clipboard =
                        application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("msg", formattedMessage)
                    clipboard.setPrimaryClip(clip)
                    send(ChatEvent.Internal.TextCopied)
                }
            }
        }


    private fun getMessages(showLoad: Boolean): Flow<ChatEvent> {
        val messages = messageRepository.getMessages(topicId.toDomain(), pageSize = PAGE_SIZE)
        val ownUserId = usersRepository.getOwnUserId()

        return combine(messages, ownUserId) { messagedRes, ownUserIdRes ->
            when (messagedRes) {
                is ResultState.Error -> {
                    ChatEvent.Internal.ErrorLoading(UiText.Resource(R.string.error))
                }

                is ResultState.Loading -> {
                    ChatEvent.Internal.Loading
                }

                is ResultState.Success -> {
                    when (ownUserIdRes) {
                        is ResultState.Error -> {
                            ChatEvent.Internal.ErrorLoading(UiText.Resource(R.string.error))
                        }

                        is ResultState.Loading -> {
                            ChatEvent.Internal.Loading
                        }

                        is ResultState.Success -> {
                            ChatEvent.Internal.ChatLoaded(
                                messagedRes.data.toChatItemList(
                                    ownUserIdRes.data
                                )
                            )
                        }
                    }
                }
            }
        }.run {
            if (showLoad) this
            else this.filter { it !is ChatEvent.Internal.Loading }
        }
    }

    private fun loadMore(): Flow<ChatEvent> {
        val moreMessages = messageRepository.requestMore(topicId.toDomain(), PAGE_SIZE)
        val ownUserId = usersRepository.getOwnUserId()

        return combine(moreMessages, ownUserId) { messageRes, ownUserIdRes ->
            when (ownUserIdRes) {
                is ResultState.Error -> {
                    ChatEvent.Internal.ErrorLoading(UiText.Resource(R.string.error))
                }

                is ResultState.Loading -> {
                    ChatEvent.Internal.Loading
                }

                is ResultState.Success -> {
                    ChatEvent.Internal.ChatLoaded(
                        messageRes.toChatItemList(ownUserIdRes.data)
                    )
                }
            }
        }.run {
            this.filter { it !is ChatEvent.Internal.Loading }
        }
    }

    private fun deleteMessage(messageId: String): Flow<ChatEvent> =
        channelFlow {
            messageRepository.deleteMessage(messageId.toInt()).collectLatest { result ->
                result.onSuccess {
                    ChatEvent.Internal.MessageDeleted
                }.onError { _, _ ->
                    send(ChatEvent.Internal.ActionError("Can't delete message"))
                }
            }
        }

    private fun onEditButtonClick(idMessage: String): Flow<ChatEvent> =
        channelFlow {
            messageRepository.getMessageById(idMessage).collectLatest { result ->
                result.onSuccess { message ->
                    send(ChatEvent.Internal.MessageLoaded(message.message))
                }.onError { _, _ ->
                    send(ChatEvent.Internal.ActionError("Can't edit message"))
                }
            }
        }

    private fun onChangeTopicButtonClick(messageId: String): Flow<ChatEvent> =
        channelFlow {
            send(ChatEvent.Internal.TopicBarIsReady(messageId, topicId))
        }

    private fun onTopicSelected(messageId: String, topicId: TopicId): Flow<ChatEvent> =
        channelFlow {
            messageRepository.changeMessageTopic(messageId.toInt(), topicId.topicName).collectLatest { result ->
                result.onSuccess {
                    ChatEvent.Internal.TopicChanged
                }.onError { _, _ ->
                    send(ChatEvent.Internal.ActionError("Can't change topic"))
                }
            }
        }

    companion object {
        private const val VISIBLE_THRESHOLD = 5
        private const val PAGE_SIZE = 20
        fun shouldFetchMore(firstVisibleItemPosition: Int, total: Int) =
            total - firstVisibleItemPosition <= VISIBLE_THRESHOLD
    }
}
