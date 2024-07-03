package com.example.channels.presentation.new_channel.di

import androidx.lifecycle.ViewModel
import kotlin.properties.Delegates.notNull

class NewChannelComponentViewModel : ViewModel() {
    val newChannelComponent: NewChannelComponent = DaggerNewChannelComponent.factory().create(
        NewChannelDepsProvider.deps)
}

interface NewChannelDepsProvider {
    val deps: NewChannelDeps

    companion object : NewChannelDepsProvider by NewChannelDepsStore
}

object NewChannelDepsStore : NewChannelDepsProvider {
    override var deps: NewChannelDeps by notNull()
}
