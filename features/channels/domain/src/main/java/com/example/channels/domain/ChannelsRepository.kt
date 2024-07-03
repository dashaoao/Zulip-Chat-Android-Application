package com.example.channels.domain

import com.example.common.core.result_state.ResultFlow
import kotlinx.coroutines.flow.Flow

interface ChannelsRepository {
    suspend fun getAllChannels(): Flow<List<Channel>>
    suspend fun getSubscribedChannels(): Flow<List<Channel>>
    fun getChannelById(channelId: String): ResultFlow<Channel>
    suspend fun getChannelTopics(channelId: String, needRefresh: Boolean): Flow<List<Topic>>
    fun addNewChannel(name: String): ResultFlow<Unit>
}
