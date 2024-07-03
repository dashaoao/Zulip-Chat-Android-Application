package com.example.channels.presentation

import androidx.media3.common.util.UnstableApi
import com.example.channels.presentation.model.ChannelCommand
import com.example.channels.presentation.model.ChannelEffect
import com.example.channels.presentation.model.ChannelEvent
import com.example.channels.presentation.model.ChannelUIState
import com.example.channels.presentation.model.ChannelsItem
import com.example.channels.presentation.model.StreamsType
import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

@UnstableApi
class ChannelReducer : DslReducer<ChannelEvent, ChannelUIState, ChannelEffect, ChannelCommand>() {
    override fun Result.reduce(event: ChannelEvent): Any = when (event) {

        ChannelEvent.UI.Initialize -> {
            state { ChannelUIState() }
            commands {
                +ChannelCommand.SubscribeToSearchQueryState
                +ChannelCommand.GetChannels(state.type == StreamsType.SUBSCRIBED)
            }
        }

        is ChannelEvent.UI.ChannelClick -> {
            val expanded = state.channels.find {
                it is ChannelsItem.Channel && it.id == event.channelId && it.expanded
            }

            if (expanded != null) {
                val channelsCollapsed = state.channels.mapNotNull {
                    when {
                        it is ChannelsItem.Channel && it.id == event.channelId -> it.copy(expanded = false)
                        it is ChannelsItem.Topic && it.topicId.streamId == event.channelId -> null
                        else -> it
                    }
                }
                state { state.copy(channels = channelsCollapsed) }
            } else {
                val channelsLoading = state.channels.map {
                    if (it is ChannelsItem.Channel && it.id == event.channelId)
                        it.copy(isTopicLoading = true, expanded = true)
                    else it
                }
                state { state.copy(channels = channelsLoading) }
                commands { +ChannelCommand.GetChannelTopics(event.channelId) }
            }
        }

        is ChannelEvent.UI.SearchTextChanged -> {
            state { state.copy(isLoading = true) }
            commands {
                +ChannelCommand.PerformSearch(
                    event.text,
                    state.type == StreamsType.SUBSCRIBED
                )
            }
        }

        is ChannelEvent.UI.TopicClick -> {
            commands { +ChannelCommand.NavigateToTopicChat(event.topicId) }
        }

        is ChannelEvent.UI.GetChannels -> {
            state {
                state.copy(
                    isLoading = true,
                    type = if (event.isSubscribed) StreamsType.SUBSCRIBED else StreamsType.ALL
                )
            }
            commands { +ChannelCommand.GetChannels(event.isSubscribed) }
        }

        is ChannelEvent.Internal.Loading -> {
            state { state.copy(isLoading = true) }
        }

        is ChannelEvent.Internal.ChannelsLoaded -> {
            val topics = state.channels.filterIsInstance<ChannelsItem.Topic>()
            val mergedChannels = buildList {
                event.data.forEach { channel ->
                    val expanded = state.channels.firstOrNull { it is ChannelsItem.Channel && it.id == channel.id && it.expanded } != null
                    add(channel.copy(expanded = expanded))
                    addAll(topics.filter { it.topicId.streamId == channel.id })
                }
            }
            state { state.copy(channels = mergedChannels, isLoading = false) }
        }

        is ChannelEvent.Internal.ErrorLoading -> {
            state { state.copy(isLoading = false) }
            effects { +ChannelEffect.Error(event.msg) }
        }

        is ChannelEvent.Internal.TopicsLoaded -> {
            val channelsWithTopics = buildList {
                state.channels.filter {
                    it !is ChannelsItem.Topic || it.topicId.streamId != event.channelId
                }.forEach {
                    if (it is ChannelsItem.Channel && it.id == event.channelId && it.expanded) {
                        add(it.copy(isTopicLoading = false))
                        addAll(event.data)
                    } else add(it)
                }
            }
            state { state.copy(channels = channelsWithTopics) }
        }
    }
}
