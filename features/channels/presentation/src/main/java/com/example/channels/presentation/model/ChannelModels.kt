package com.example.channels.presentation.model

import androidx.annotation.StringRes
import com.example.common.ui.R
import com.example.common.ui.TopicId
import com.example.common.ui.UiText

data class ChannelUIState(
    val type: StreamsType = StreamsType.SUBSCRIBED,
    val channels: List<ChannelsItem> = emptyList(),
    val isSearching: Boolean = false,
    val isLoading: Boolean = false,
)

enum class StreamsType(
    @StringRes val textResId: Int,
) {
    SUBSCRIBED(textResId = R.string.subscribed),
    ALL(textResId = R.string.all_streams),
}

sealed interface ChannelEvent {
    sealed interface UI : ChannelEvent {
        data object Initialize : UI
        class GetChannels(val isSubscribed: Boolean) : UI
        class ChannelClick(val channelId: String, val currentData: List<ChannelsItem>) : UI
        class TopicClick(val topicId: TopicId) : UI
        class SearchTextChanged(val text: String) : UI
    }

    sealed interface Internal : ChannelEvent {
        class ChannelsLoaded(val data: List<ChannelsItem.Channel>) : Internal
        class TopicsLoaded(val channelId: String, val data: List<ChannelsItem.Topic>) : Internal
        data object Loading : Internal
        class ErrorLoading(val msg: UiText) : Internal
    }
}

sealed class ChannelEffect {
    class Error(val msg: UiText) : ChannelEffect()
}

sealed class ChannelCommand {
    class GetChannels(val isSubscribed: Boolean) : ChannelCommand()
    class PerformSearch(val text: String, val isSubscribed: Boolean) : ChannelCommand()
    data object SubscribeToSearchQueryState : ChannelCommand()
    class GetChannelTopics(val channelId: String) : ChannelCommand()
    class NavigateToTopicChat(val topicId: TopicId) : ChannelCommand()
}
