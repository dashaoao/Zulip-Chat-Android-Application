package com.example.channels.data.di

import com.example.channels.data.api.ChannelsApi
import com.example.channels.data.database.ChannelsDao
import com.example.chat.data.api.ChatApi
import com.example.chat.data.database.ChatDao
import com.example.common.data.events.EventsApi

interface ChannelsDataDeps {
    fun channelsApi(): ChannelsApi
    fun chatApi(): ChatApi
    fun eventsApi(): EventsApi
    fun channelsDao(): ChannelsDao
    fun chatDao(): ChatDao
}
