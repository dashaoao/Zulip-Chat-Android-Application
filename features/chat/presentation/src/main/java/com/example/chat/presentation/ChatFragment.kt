package com.example.chat.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.chat.presentation.di.ChatComponentViewModel
import com.example.chat.presentation.model.ChatEffect
import com.example.chat.presentation.model.ChatEvent
import com.example.chat.presentation.model.ChatUiState
import com.example.chat.presentation.ui.ChatLayout
import com.example.common.ui.TopicId
import com.example.common.ui.effect
import com.example.common.ui.state
import com.example.common.ui.theme.ChatTheme
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import javax.inject.Inject

class ChatFragment() : ElmFragment<ChatEvent, ChatEffect, ChatUiState>() {

    override val initEvent: ChatEvent = ChatEvent.UI.Initialize

    private val topicId: TopicId by lazy {
        arguments?.getParcelable<TopicId>(TOPIC_ID) ?: throw IllegalArgumentException()
    }

    @Inject
    lateinit var factory: ChatStoreFactory.Factory
    override val storeHolder =
        LifecycleAwareStoreHolder(lifecycle) { factory.create(topicId).create() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ViewModelProvider(this).get<ChatComponentViewModel>().chatComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ChatTheme {
                    val state by store.state()
                    val effect by store.effect()
                    ChatLayout(
                        state = state,
                        effect = effect,
                        onArrowBackClick = { store.accept(ChatEvent.UI.OnArrowBackClick) },
                        onTextChange = { store.accept(ChatEvent.UI.OnMessageChange(it)) },
                        onClick = { store.accept(ChatEvent.UI.OnButtonClick) },
                        onLongClick = { idMessage, isOwnMessage ->
                            store.accept(ChatEvent.UI.OnLongClick(idMessage, isOwnMessage))
                        },
                        onAddClick = { store.accept(ChatEvent.UI.OnAddReactionClick(it)) },
                        onDismissRequest = { store.accept(ChatEvent.UI.OnDismissRequest) },
                        onEmojiClick = { store.accept(ChatEvent.UI.OnEmojiClick(it)) },
                        onReactionClick = { idMessage, reaction ->
                            store.accept(ChatEvent.UI.OnReactionClick(idMessage, reaction))
                        },
                        onLastVisibleItemChange = { store.accept(ChatEvent.UI.Scroll(it)) },
                        onCopyButtonClick = { store.accept(ChatEvent.UI.OnCopyButtonClick) },
                        onDeleteButtonClick = { store.accept(ChatEvent.UI.OnDeleteButtonClick) },
                        onEditButtonClick = { store.accept(ChatEvent.UI.OnEditButtonClick) },
                        onTopicSelected = { store.accept(ChatEvent.UI.OnTopicSelected(it)) },
                        onChangeTopicClick = { store.accept(ChatEvent.UI.OnChangeTopicClick) },
                    )
                }
            }
        }
    }

    override fun render(state: ChatUiState) = Unit

    companion object {
        const val TOPIC_ID = "topicId"
        const val messageListTestTag = "messageListTestTag"
        const val reactionListTestTag = "reactionListTestTag"
        const val reactionTestTag = "reactionListTestTag"
        const val messageInputFieldTestTag = "messageInputFieldTestTag"
        const val messageSendButtonTestTag = "messageSendButtonTestTag"
        const val titleTestTag = "titleTestTag"
        const val chatItemTestTag = "chatItemTestTag"
        fun newInstance(topicId: TopicId): ChatFragment {
            return ChatFragment().apply {
                arguments = bundleOf(TOPIC_ID to topicId)
            }
        }
    }
}
