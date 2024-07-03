package com.example.channels.presentation.new_channel.di

import com.example.channels.domain.ChannelsRepository

interface NewChannelDeps {
    fun channelsRepository(): ChannelsRepository
}
