package com.example.chat.presentation.di

import androidx.lifecycle.ViewModel
import kotlin.properties.Delegates

internal class ChatComponentViewModel : ViewModel() {
    val chatComponent: ChatComponent = DaggerChatComponent.factory().create(ChatDepsProvider.deps)
}

interface ChatDepsProvider {
    val deps: ChatPresentationDeps

    companion object : ChatDepsProvider by ChatDepsStore
}

object ChatDepsStore : ChatDepsProvider {
    override var deps: ChatPresentationDeps by Delegates.notNull()
}
