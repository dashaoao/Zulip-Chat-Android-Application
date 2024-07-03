package com.example.chat.data.di

import com.example.chat.data.MessageRepositoryImpl
import com.example.chat.data.api.events.MessageEventHandler
import com.example.chat.data.api.events.MessageEventHandlerImpl
import com.example.chat.domain.MessageRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ChatDataModule {
    @Singleton
    @Binds
    fun bindMessageRepository(impl: MessageRepositoryImpl): MessageRepository

    @Singleton
    @Binds
    fun bindMessageEventHandler(impl: MessageEventHandlerImpl): MessageEventHandler

}
