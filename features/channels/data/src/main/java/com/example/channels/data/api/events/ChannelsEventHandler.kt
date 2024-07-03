package com.example.channels.data.api.events

import com.example.channels.domain.ChannelsEvent
import kotlinx.coroutines.flow.Flow

interface ChannelsEventHandler {
    fun getEvents(): Flow<List<ChannelsEvent>>
}
