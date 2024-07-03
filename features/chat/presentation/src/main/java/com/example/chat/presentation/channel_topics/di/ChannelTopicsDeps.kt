package com.example.chat.presentation.channel_topics.di

import com.example.channels.domain.usecase.GetChannelTopicsUseCase

interface ChannelTopicsDeps {
    fun getChannelTopicsUseCase(): GetChannelTopicsUseCase
}
