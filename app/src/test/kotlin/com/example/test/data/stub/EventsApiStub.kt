package com.example.test.data.stub

import com.example.common.data.events.EventsApi
import com.example.common.data.events.model.GetEventResponse
import com.example.common.data.events.model.RegisterEventQueueResponse

class EventsApiStub : EventsApi {

    override suspend fun registerQueue(
        allPublicStreams: Boolean,
        eventTypes: String,
        narrow: String
    ): RegisterEventQueueResponse =
        RegisterEventQueueResponse("0", -1)

    override suspend fun getEvents(queueId: String, id: Int, dontBlock: Boolean): GetEventResponse =
        GetEventResponse(emptyList())
}
