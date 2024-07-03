package com.example.chat.presentation.channel_topics.di

import androidx.lifecycle.ViewModel
import kotlin.properties.Delegates

class ChannelTopicsComponentViewModel : ViewModel() {
    val channelTopicsComponent: ChannelTopicsComponent = DaggerChannelTopicsComponent.factory().create(
        ChannelTopicsDepsProvider.deps
    )
}

interface ChannelTopicsDepsProvider {
    val deps: ChannelTopicsDeps

    companion object : ChannelTopicsDepsProvider by ChannelTopicsDepsStore
}

object ChannelTopicsDepsStore : ChannelTopicsDepsProvider {
    override var deps: ChannelTopicsDeps by Delegates.notNull()
}
