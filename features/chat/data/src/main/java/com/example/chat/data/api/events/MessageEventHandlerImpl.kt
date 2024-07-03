package com.example.chat.data.api.events

import android.util.Log
import com.example.chat.domain.MessageEvent
import com.example.common.core.TopicId
import com.example.common.data.events.EventsApi
import com.example.common.util.runCatchingNonCancellation
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import javax.inject.Inject

class MessageEventHandlerImpl @Inject constructor(
    private val eventsApi: EventsApi
) : MessageEventHandler {

    private var lastEventId: Int = -1
    private var eventQueueId: String? = null
    private val mInterval = 1000L

    private suspend fun register(topicId: TopicId) {
        runCatchingNonCancellation {
            eventsApi.registerQueue(
                allPublicStreams = true,
                eventTypes = """["message", "delete_message", "update_message", "reaction"]""",
                narrow = """[["topic", "${topicId.topicName}"]]"""
            ).let {
                eventQueueId = it.queueId
                lastEventId = it.lastEventId
            }
        }.onFailure {
            Log.d("StreamEventHandler", "Registration failed", it)
        }
    }

    override fun getEvents(topicId: TopicId): Flow<List<MessageEvent>> = flow {
        register(topicId)
        while (currentCoroutineContext().isActive) {
            runCatchingNonCancellation {
                eventQueueId?.let { queueId ->
                    val events = eventsApi.getEvents(queueId, lastEventId, false).events
                    if (events.isNotEmpty()) {
                        lastEventId = events.last().id
                        emit(events.map { it.toMessageEvent() })
                    }
                }
                delay(mInterval)
            }.onFailure {
                Log.d("MessageEventHandler", "Run failed", it)
            }
        }
    }
}
