package com.example.channels.domain.usecase

import com.example.channels.domain.Channel
import com.example.channels.domain.ChannelsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class GetChannelsUseCase(
    private val channelsRepository: ChannelsRepository,
    private val getTopicsUseCase: GetTopicsUseCase,
) {
    suspend operator fun invoke(
        query: String,
        searchInSubscribed: Boolean,
        refreshTopicsForChannels: Set<String>,
    ): Flow<List<Channel>> {
        val unfilteredChannels = if (searchInSubscribed)
            channelsRepository.getSubscribedChannels()
        else
            channelsRepository.getAllChannels()

        val filteredChannels = unfilteredChannels.map { response ->
            if (query.isEmpty())
                response
            else
                response.performSearch(query)
        }

        return filteredChannels.flatMapLatest { channels ->
            val flows = channels.map { channel ->
                getTopicsUseCase(channelId = channel.id, needRefresh = channel.id in refreshTopicsForChannels).map {
                    channel.copy(topics = it)
                }
            }
            if (flows.isEmpty()) flowOf(emptyList())
            else combine(flows) { it.toList() }
        }
    }

    private fun List<Channel>.performSearch(query: String) = this.filter {
        it.title.lowercase().startsWith(query.lowercase())
    }
}
