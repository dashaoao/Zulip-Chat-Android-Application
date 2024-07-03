package com.example.chat.presentation

import android.text.Html
import androidx.media3.common.util.UnstableApi
import com.example.chat.presentation.ChatActor.Companion.shouldFetchMore
import com.example.chat.presentation.model.ButtonState
import com.example.chat.presentation.model.ChatCommand
import com.example.chat.presentation.model.ChatEffect
import com.example.chat.presentation.model.ChatEvent
import com.example.chat.presentation.model.ChatUiState
import com.example.chat.presentation.model.DialogState
import com.example.chat.presentation.model.EditingMessage
import com.example.common.ui.UiText
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

@UnstableApi
class ChatReducer : DslReducer<ChatEvent, ChatUiState, ChatEffect, ChatCommand>() {
    override fun Result.reduce(event: ChatEvent): Any? = when (event) {

        is ChatEvent.UI.Initialize -> {
            state { ChatUiState(isLoading = true) }
            commands {
                +ChatCommand.GetMessages
            }
        }

        is ChatEvent.UI.Scroll -> {
            commands {
                if (shouldFetchMore(event.firstVisibleItemPosition, state.chatItems.size)) {
                    +ChatCommand.LoadMore
                }
            }
        }

        is ChatEvent.Internal.ChatLoaded -> {
            state {
                state.copy(isLoading = false, chatItems = event.data)
            }
        }

        is ChatEvent.Internal.ErrorLoading -> {
            effects {
                +ChatEffect.Error(event.msg)
            }
        }

        ChatEvent.Internal.Loading -> {
            state {
                state.copy(isLoading = true)
            }
        }

        is ChatEvent.UI.OnArrowBackClick -> {
            commands {
                +ChatCommand.OnArrowBackClick
            }
        }

        is ChatEvent.UI.OnMessageChange -> {
            state {
                state.copy(
                    message = event.text,
                    buttonState = if (event.text.isBlank()) {
                        ButtonState.Attach
                    } else if (state.editingMessage != null) {
                        ButtonState.Done
                    } else {
                        ButtonState.Send
                    },
                )
            }
        }

        is ChatEvent.UI.OnReactionClick -> {
            commands {
                +ChatCommand.OnReactionClick(event.idMessage, event.reactionUI)
            }
        }

        is ChatEvent.UI.OnButtonClick -> {
            when (state.buttonState) {
                ButtonState.Send -> {
                    val messageToSend = state.message
                    state {
                        state.copy(message = "", buttonState = ButtonState.Attach)
                    }
                    commands {
                        +ChatCommand.OnSendButtonClick(messageToSend)
                    }
                }

                ButtonState.Attach -> {
                    state {
                        state
                    }
                }

                ButtonState.Done -> {
                    val message = state.message
                    commands {
                        +state.editingMessage?.let { ChatCommand.OnDoneButtonClick(it.id, message) }
                    }
                    state {
                        state.copy(
                            message = "",
                            buttonState = ButtonState.Attach,
                            editingMessage = null
                        )
                    }
                }
            }
        }

        is ChatEvent.UI.OnDismissRequest -> {
            state {
                state.copy(dialogState = DialogState.NoDialog)
            }
        }

        is ChatEvent.UI.OnEmojiClick -> {
            when (state.dialogState) {
                is DialogState.EmojiPick -> {
                    val emojiPickState = state.dialogState as DialogState.EmojiPick
                    state {
                        state.copy(dialogState = DialogState.NoDialog)
                    }
                    commands {
                        +ChatCommand.OnEmojiClick(event.emoji, emojiPickState.idMessage)
                    }
                }

                is DialogState.OwnActionBar -> {
                    val actionBarState = state.dialogState as DialogState.OwnActionBar
                    state {
                        state.copy(dialogState = DialogState.NoDialog)
                    }
                    commands {
                        +ChatCommand.OnEmojiClick(event.emoji, actionBarState.idMessage)
                    }
                }

                is DialogState.ActionBar -> {
                    val actionBarState = state.dialogState as DialogState.ActionBar
                    state {
                        state.copy(dialogState = DialogState.NoDialog)
                    }
                    commands {
                        +ChatCommand.OnEmojiClick(event.emoji, actionBarState.idMessage)
                    }
                }

                else -> {
                    state {
                        state.copy(dialogState = DialogState.NoDialog)
                    }
                }
            }
        }

        is ChatEvent.UI.OnLongClick -> {
            commands {
                if (event.isOwnMessage) {
                    +ChatCommand.PrepareOwnMessageActionBar(event.idMessage)
                } else {
                    +ChatCommand.PrepareMessageActionBar(event.idMessage)
                }
            }
        }

        is ChatEvent.UI.OnAddReactionClick -> {
            commands {
                +ChatCommand.PrepareReactionDialog(event.idMessage)
            }
        }

        is ChatEvent.UI.OnChangeTopicClick -> {
            val dialogState = state.dialogState as DialogState.OwnActionBar
            commands {
                +ChatCommand.OnChangeTopicButtonClick(dialogState.idMessage)
            }
        }

        is ChatEvent.Internal.TopicBarIsReady -> {
            state {
                state.copy(
                    dialogState = DialogState.TopicBar(event.idMessage, event.topicId)
                )
            }
        }

        is ChatEvent.Internal.OwnActionBarIsReady -> {
            state {
                state.copy(
                    dialogState = DialogState.OwnActionBar(
                        event.idMessage,
                        event.data
                    )
                )
            }
        }

        is ChatEvent.Internal.ActionBarIsReady -> {
            state {
                state.copy(
                    dialogState = DialogState.ActionBar(
                        event.idMessage,
                        event.data
                    )
                )
            }
        }

        is ChatEvent.Internal.ReactionDiaogIsReady -> {
            state {
                state.copy(
                    dialogState = DialogState.EmojiPick(
                        event.idMessage,
                        event.data
                    )
                )
            }
        }

        ChatEvent.Internal.TextCopied -> {
            effects {
                +ChatEffect.Copied(UiText.RawString("Copied"))
            }
        }

        is ChatEvent.UI.OnCopyButtonClick -> {
            if (state.dialogState is DialogState.OwnActionBar) {
                val ownActionBarState = state.dialogState as DialogState.OwnActionBar
                state {
                    state.copy(dialogState = DialogState.NoDialog)
                }
                commands {
                    +ChatCommand.OnCopyButtonClick(ownActionBarState.idMessage)
                }
            } else if (state.dialogState is DialogState.ActionBar) {
                val actionBarState = state.dialogState as DialogState.ActionBar
                state {
                    state.copy(dialogState = DialogState.NoDialog)
                }
                commands {
                    +ChatCommand.OnCopyButtonClick(actionBarState.idMessage)
                }
            } else {
                state {
                    state.copy(dialogState = DialogState.NoDialog)
                }
            }
        }

        ChatEvent.Internal.MessageDeleted -> {
            state {
                state
            }
        }

        ChatEvent.UI.OnDeleteButtonClick -> {
            if (state.dialogState is DialogState.OwnActionBar) {
                val ownActionBarState = state.dialogState as DialogState.OwnActionBar
                state {
                    state.copy(dialogState = DialogState.NoDialog)
                }
                commands {
                    +ChatCommand.OnDeleteButtonClick(ownActionBarState.idMessage)
                }
            } else {
                state {
                    state.copy(dialogState = DialogState.NoDialog)
                }
            }
        }

        ChatEvent.Internal.MessageEdited -> {
            state {
                state
            }
        }

        ChatEvent.UI.OnEditButtonClick -> {
            if (state.dialogState is DialogState.OwnActionBar) {
                val ownActionBarState = state.dialogState as DialogState.OwnActionBar
                state {
                    state.copy(
                        editingMessage = EditingMessage(
                            id = ownActionBarState.idMessage,
                            initialText = ""
                        )
                    )
                }
                commands {
                    +ChatCommand.OnEditButtonClick(ownActionBarState.idMessage)
                }
            } else {
                state {
                    state.copy(dialogState = DialogState.NoDialog)
                }
            }
        }

        is ChatEvent.Internal.MessageLoaded -> {
            state {
                state.copy(
                    message = Html.fromHtml(event.newMessage).toString().trim(),
                    dialogState = DialogState.NoDialog,
                    buttonState = ButtonState.Done,
                )
            }
        }

        is ChatEvent.Internal.ActionError -> {
            effects {
                +ChatEffect.Error(UiText.RawString(event.msg))
            }
        }

        is ChatEvent.UI.OnTopicSelected -> {
            if (state.dialogState is DialogState.TopicBar) {
                val dialogState = state.dialogState as DialogState.TopicBar
                commands {
                    +ChatCommand.OnTopicSelected(dialogState.idMessage, event.idTopic)
                }
                state {
                    state.copy(
                        dialogState = DialogState.NoDialog,
                    )
                }
            } else {
                state {
                    state.copy(
                        dialogState = DialogState.NoDialog,
                    )
                }
            }
        }

        ChatEvent.Internal.TopicChanged -> {
            state {
                state
            }
        }
    }
}
