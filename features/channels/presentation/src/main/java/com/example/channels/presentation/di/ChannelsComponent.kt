package com.example.channels.presentation.di

import com.example.channels.presentation.ChannelsFragment
import dagger.Component

@ChannelsScope
@Component(
    dependencies = [ChannelsPresentationDeps::class],
)
interface ChannelsComponent {
    fun inject(fragment: ChannelsFragment)

    @Component.Factory
    interface Factory {
        fun create(channelsDeps: ChannelsPresentationDeps): ChannelsComponent
    }
}
