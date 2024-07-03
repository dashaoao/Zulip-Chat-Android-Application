package com.example.channels.data.di

import com.example.channels.data.ChannelsRepositoryImpl
import com.example.channels.data.api.events.ChannelsEventHandler
import com.example.channels.data.api.events.ChannelsEventHandlerImpl
import com.example.channels.domain.ChannelsRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ChannelsDataModule {
    @Singleton
    @Binds
    fun bindChannelsRepository(impl: ChannelsRepositoryImpl): ChannelsRepository

    @Singleton
    @Binds
    fun bindChannelsEventHandler(impl: ChannelsEventHandlerImpl): ChannelsEventHandler
}
