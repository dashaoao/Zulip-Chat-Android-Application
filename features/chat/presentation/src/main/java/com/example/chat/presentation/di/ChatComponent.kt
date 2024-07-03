package com.example.chat.presentation.di

import com.example.chat.presentation.ChatFragment
import dagger.Component

@ChatScope
@Component(dependencies = [ChatPresentationDeps::class])
interface ChatComponent {

    fun inject(fragment: ChatFragment)

    @Component.Factory
    interface Factory {
        fun create(chatPresentationDeps: ChatPresentationDeps): ChatComponent
    }

}
