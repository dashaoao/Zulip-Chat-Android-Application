package com.example.channels.data

import com.example.channels.data.api.ChannelsApi
import com.example.channels.data.api.events.ChannelsEventHandler
import com.example.channels.data.api.model.SubscriptionParam
import com.example.channels.data.api.model.toChannelEntity
import com.example.channels.data.api.model.toDomain
import com.example.channels.data.api.model.toSubscribedChannelEntity
import com.example.channels.data.database.ChannelsDao
import com.example.channels.data.database.model.toDomain
import com.example.channels.data.database.model.toEntity
import com.example.channels.domain.Channel
import com.example.channels.domain.ChannelsRepository
import com.example.channels.domain.Topic
import com.example.common.core.result_state.ResultFlow
import com.example.common.core.result_state.flowState
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class ChannelsRepositoryImpl @Inject constructor(
    private val channelsApi: ChannelsApi,
    private val channelsDao: ChannelsDao,
    private val channelsEventHandler: ChannelsEventHandler
) : ChannelsRepository {

    override suspend fun getAllChannels(): Flow<List<Channel>> {
        return getCacheAllChannels().also {
            getFreshAllChannels()
            CoroutineScope(coroutineContext).launch {
                channelsEventHandler.getEvents().collect {
                    getFreshAllChannels()
                }
            }
        }
    }

    private fun getCacheAllChannels(): Flow<List<Channel>> =
        channelsDao.getChannels().map { channels -> channels.map { it.toDomain() } }
            .distinctUntilChanged()

    private suspend fun getFreshAllChannels() = CoroutineScope(coroutineContext).launch {
        val freshChannels = channelsApi.getAllChannels().streams.map { it.toChannelEntity() }
        channelsDao.updateChannels(freshChannels)
    }

    override suspend fun getSubscribedChannels(): Flow<List<Channel>> {
        return getCacheSubscribedChannels().also {
            getFreshSubscribedChannels()
            CoroutineScope(coroutineContext).launch {
                channelsEventHandler.getEvents().collect {
                    getFreshSubscribedChannels()
                }
            }
        }
    }

    private fun getCacheSubscribedChannels(): Flow<List<Channel>> =
        channelsDao.getSubscribedChannels().map { channels -> channels.map { it.toDomain() } }
            .distinctUntilChanged()

    private suspend fun getFreshSubscribedChannels() = CoroutineScope(coroutineContext).launch {
        val freshSubscribedChannels =
            channelsApi.getSubscribedChannels().streams.map { it.toSubscribedChannelEntity() }
        channelsDao.updateSubscribedChannels(freshSubscribedChannels)
    }

    override fun getChannelById(channelId: String): ResultFlow<Channel> =
        flowState {
            channelsApi.getChannelById(channelId).toDomain()
        }

    override suspend fun getChannelTopics(
        channelId: String,
        needRefresh: Boolean
    ): Flow<List<Topic>> = getCacheChannelTopic(channelId).also {
        if (needRefresh) {
            CoroutineScope(coroutineContext).launch {
                getFreshChannelTopic(channelId)
            }
        }
    }

    override fun addNewChannel(name: String): ResultFlow<Unit> {
        return flowState {
            channelsApi.addNewChannel(
                subscriptions = Gson().toJson(listOf(SubscriptionParam(name = name)))
            )
        }
    }

    private fun getCacheChannelTopic(channelId: String): Flow<List<Topic>> =
        channelsDao.getTopics(channelId).map { list ->
            list.map { it.toDomain(channelId) }
        }

    private suspend fun getFreshChannelTopic(channelId: String) {
        val fresh = channelsApi.getStreamTopics(channelId).topics.map {
            it.toDomain(channelId)
        }
        updateTopics(fresh, channelId)
    }

    private suspend fun updateTopics(topics: List<Topic>, channelId: String) =
        channelsDao.updateTopics(topics.map { it.toEntity() }, channelId)

}
