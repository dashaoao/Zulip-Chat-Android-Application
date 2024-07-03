package com.example.chat.presentation.channel_topics.di

import com.example.chat.presentation.channel_topics.ChannelTopicsViewModel
import dagger.Component

@Component(dependencies = [ChannelTopicsDeps::class])
@ChannelTopicsScope
interface ChannelTopicsComponent {

    @Component.Factory
    interface Factory {
        fun create(channelTopicsDeps: ChannelTopicsDeps): ChannelTopicsComponent
    }

    fun getViewModel() : ChannelTopicsViewModel
}
