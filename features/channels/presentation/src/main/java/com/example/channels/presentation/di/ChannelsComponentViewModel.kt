package com.example.channels.presentation.di

import androidx.lifecycle.ViewModel
import kotlin.properties.Delegates

internal class ChannelsComponentViewModel : ViewModel() {
    val channelsComponent: ChannelsComponent = DaggerChannelsComponent.factory().create(
        ChannelsDepsProvider.deps)
}

interface ChannelsDepsProvider {
    val deps: ChannelsPresentationDeps

    companion object : ChannelsDepsProvider by ChannelsDepsStore
}

object ChannelsDepsStore : ChannelsDepsProvider {
    override var deps: ChannelsPresentationDeps by Delegates.notNull()
}
