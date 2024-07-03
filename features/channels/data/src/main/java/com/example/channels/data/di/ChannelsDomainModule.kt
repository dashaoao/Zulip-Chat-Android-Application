package com.example.channels.data.di

import com.example.channels.domain.ChannelsRepository
import com.example.channels.domain.usecase.GetChannelTopicsUseCase
import com.example.channels.domain.usecase.GetChannelsUseCase
import com.example.channels.domain.usecase.GetTopicsUseCase
import com.example.chat.domain.MessageRepository
import dagger.Module
import dagger.Provides

@Module
class ChannelsDomainModule {
    @Provides
    fun provideGetChannelsUseCase(
        channelsRepository: ChannelsRepository,
        getTopicsUseCase: GetTopicsUseCase,
    ) : GetChannelsUseCase =
        GetChannelsUseCase(channelsRepository, getTopicsUseCase)

    @Provides
    fun provideGetTopicsUseCase(
        channelsRepository: ChannelsRepository,
        messageRepository: MessageRepository,
    ): GetTopicsUseCase =
        GetTopicsUseCase(channelsRepository, messageRepository)

    @Provides
    fun provideGetChannelTopicsUseCase(
        channelsRepository: ChannelsRepository,
    ): GetChannelTopicsUseCase =
        GetChannelTopicsUseCase(channelsRepository)
}
