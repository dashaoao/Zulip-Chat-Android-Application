package com.example.channels.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.viewModelScope
import com.example.channels.presentation.di.ChannelsComponentViewModel
import com.example.channels.presentation.model.ChannelEffect
import com.example.channels.presentation.model.ChannelEvent
import com.example.channels.presentation.model.ChannelUIState
import com.example.channels.presentation.ui.ChannelsLayout
import com.example.common.ui.effect
import com.example.common.ui.state
import com.example.common.ui.theme.ChatTheme
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import javax.inject.Inject

class ChannelsFragment() : ElmFragment<ChannelEvent, ChannelEffect, ChannelUIState>() {

    override val initEvent: ChannelEvent = ChannelEvent.UI.Initialize
    private lateinit var componentViewModel: ChannelsComponentViewModel

    @Inject
    lateinit var factory: ChannelStoreFactory.Factory
    override val storeHolder = LifecycleAwareStoreHolder(lifecycle) {
        factory.create(componentViewModel.viewModelScope).create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        componentViewModel = ViewModelProvider(this).get<ChannelsComponentViewModel>()
        componentViewModel.channelsComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ChatTheme {
                    val state by store.state()
                    val effect by store.effect()
                    ChannelsLayout(
                        state = state,
                        effect = effect,
                        performSearch = { store.accept(ChannelEvent.UI.SearchTextChanged(it)) },
                        onChannelClick = { channelId, data ->
                            store.accept(
                                ChannelEvent.UI.ChannelClick(
                                    channelId = channelId,
                                    currentData = data,
                                )
                            )
                        },
                        getChannels = { store.accept(ChannelEvent.UI.GetChannels(it)) },
                        onTopicClick = { store.accept(ChannelEvent.UI.TopicClick(it)) },
                    )
                }
            }
        }
    }

    override fun render(state: ChannelUIState) = Unit
}
