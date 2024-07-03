package com.example.channels.presentation.new_channel.model

sealed interface NewChannelNavEvent {
    data object Exit : NewChannelNavEvent
}
