package com.example.channels.data.api.events

import android.util.Log
import com.example.channels.domain.ChannelsEvent
import com.example.common.data.events.EventsApi
import com.example.common.util.runCatchingNonCancellation
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import javax.inject.Inject

class ChannelsEventHandlerImpl @Inject constructor(
    private val eventsApi: EventsApi
) : ChannelsEventHandler {

    private var lastEventId: Int = -1
    private var eventQueueId: String? = null
    private val mInterval = 1000L

    private suspend fun register() {
        runCatchingNonCancellation {
            eventsApi.registerQueue(
                allPublicStreams = true,
                eventTypes = """["stream", "subscription"]"""
            ).let {
                eventQueueId = it.queueId
                lastEventId = it.lastEventId
            }
        }.onFailure {
            Log.d("StreamEventHandler", "Registration failed", it)
        }
    }

    override fun getEvents(): Flow<List<ChannelsEvent>> = flow {
        register()
        while (currentCoroutineContext().isActive) {
            runCatchingNonCancellation {
                eventQueueId?.let { queueId ->
                    val events = eventsApi.getEvents(queueId, lastEventId, false).events
                    if (events.isNotEmpty()) {
                        lastEventId = events.last().id
                        emit(events.map { ChannelsEvent(it.id) })
                    }
                }
                delay(mInterval)
            }.onFailure {
                Log.d("StreamEventHandler", "Run failed", it)
            }
        }
    }
}
