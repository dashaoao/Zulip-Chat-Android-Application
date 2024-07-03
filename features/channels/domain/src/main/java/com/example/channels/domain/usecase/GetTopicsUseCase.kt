package com.example.channels.domain.usecase

import com.example.channels.domain.ChannelsRepository
import com.example.channels.domain.Topic
import com.example.chat.domain.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GetTopicsUseCase(
    private val channelsRepository: ChannelsRepository,
    private val messageRepository: MessageRepository
) {

    operator fun invoke(channelId: String, needRefresh: Boolean): Flow<List<Topic>> = channelFlow {
        val currentData = mutableListOf<Topic>()
        channelsRepository.getChannelTopics(channelId, needRefresh).collectLatest { newTopicsNoCount ->
            val newTopicsOldCount = newTopicsNoCount.setMessageCount(from = currentData)
            currentData.apply {
                clear()
                addAll(newTopicsOldCount)
                send(this)
            }
            if (needRefresh) {
                newTopicsOldCount.forEachIndexed { index, topic ->
                    launch {
                        val count: Int = messageRepository.getTopicUnreadMessageCount(topic.id)
                        currentData[index] = currentData[index].copy(messageCount = count)
                        send(currentData)
                    }
                }
            }
        }
    }

    private fun List<Topic>.setMessageCount(from: List<Topic>) = map { topic ->
        topic.copy(messageCount = from.firstOrNull { it.id == topic.id }?.messageCount)
    }

}
