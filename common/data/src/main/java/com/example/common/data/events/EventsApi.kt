package com.example.common.data.events

import com.example.common.data.events.model.GetEventResponse
import com.example.common.data.events.model.RegisterEventQueueResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface EventsApi {

    @POST("register")
    suspend fun registerQueue(
        @Query("all_public_streams") allPublicStreams: Boolean,
        @Query("event_types") eventTypes: String,
        @Query("narrow") narrow: String = "[]"
    ): RegisterEventQueueResponse

    @GET("events")
    suspend fun getEvents(
        @Query("queue_id") queueId: String,
        @Query("last_event_id") id: Int,
        @Query("dont_block") dontBlock: Boolean,
    ): GetEventResponse

}
