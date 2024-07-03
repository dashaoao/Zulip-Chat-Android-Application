package com.example.channels.data.api

import com.example.channels.data.api.model.AllStreamsResponse
import com.example.channels.data.api.model.StreamDto
import com.example.channels.data.api.model.StreamTopicsResponse
import com.example.channels.data.api.model.SubscribedChannelsResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChannelsApi {
    @GET("streams")
    suspend fun getAllChannels(): AllStreamsResponse

    @GET("users/me/subscriptions")
    suspend fun getSubscribedChannels(): SubscribedChannelsResponse
    @FormUrlEncoded
    @POST("users/me/subscriptions")
    suspend fun addNewChannel(
        @Field("subscriptions") subscriptions: String,
    )

    @GET("streams/{stream_id}")
    suspend fun getChannelById(channelId: String): StreamDto

    @GET("users/me/{stream_id}/topics")
    suspend fun getStreamTopics(
        @Path(value = "stream_id", encoded = true) streamId: String
    ): StreamTopicsResponse
}
