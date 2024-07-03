package com.example.channels.domain.usecase

import com.example.channels.domain.ChannelsRepository
import com.example.channels.domain.Topic
import com.example.common.core.TopicId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest

class GetChannelTopicsUseCase(private val channelsRepository: ChannelsRepository) {
    operator fun invoke(currentTopicId: TopicId): Flow<List<Topic>> =
        channelFlow {
            channelsRepository.getChannelTopics(currentTopicId.streamId, true)
                .collectLatest { topics ->
                    val filteredTopics = topics.filter { it.title != currentTopicId.topicName }
                    send(filteredTopics)
                }
        }
}
