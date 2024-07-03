package com.example.chat.presentation

import android.app.Application
import com.example.chat.domain.MessageRepository
import com.example.chat.presentation.model.ChatUiState
import com.example.common.ui.TopicId
import com.example.users.domain.UsersRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import vivid.money.elmslie.coroutines.ElmStoreCompat

class ChatStoreFactory @AssistedInject constructor(
    @Assisted("topicId") private val topicId: TopicId,
    private val messageRepository: MessageRepository,
    private val usersRepository: UsersRepository,
    private val chatNavigation: ChatNavigation,
    private val application: Application,
) {

    fun create() = ElmStoreCompat(
        initialState = ChatUiState(),
        reducer = ChatReducer(),
        actor = ChatActor(
            chatNavigation = chatNavigation,
            messageRepository = messageRepository,
            usersRepository = usersRepository,
            topicId = topicId,
            application = application
        )
    )

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("topicId") topicId: TopicId,
        ) : ChatStoreFactory
    }
}
