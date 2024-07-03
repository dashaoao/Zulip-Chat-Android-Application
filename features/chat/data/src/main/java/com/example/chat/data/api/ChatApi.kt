package com.example.chat.data.api

import com.example.chat.data.api.model.FetchMessageResponse
import com.example.chat.data.api.model.MessagesResponse
import com.example.chat.data.api.model.SendMessageDto
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ChatApi {

    @FormUrlEncoded
    @POST("messages")
    suspend fun sendMessage(
        @FieldMap params: Map<String, String>
    ): SendMessageDto

    @DELETE("messages/{message_id}")
    suspend fun deleteMessage(
        @Path(value = "message_id", encoded = true) messageId: Int,
    )

    @FormUrlEncoded
    @PATCH("messages/{message_id}")
    suspend fun editMessage(
        @Path(value = "message_id", encoded = true) messageId: Int,
        @Field("content") content: String,
    )

    @FormUrlEncoded
    @POST("messages/{message_id}/reactions")
    suspend fun addReaction(
        @Path(value = "message_id", encoded = true) idMessage: Int,
        @FieldMap params: Map<String, String>
    )

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "messages/{message_id}/reactions", hasBody = true)
    suspend fun removeReaction(
        @Path(value = "message_id", encoded = true) messageId: Int,
        @Field("emoji_name") emojiName: String
    )
    @GET("messages")
    suspend fun getMessages(
        @QueryMap params: Map<String, String>,
        @Query("apply_markdown") applyMarkdown: Boolean = true
    ): MessagesResponse

    @GET("messages/{message_id}")
    suspend fun fetchMessage(
        @Path(value = "message_id", encoded = true) messageId: Int,
        @Query("apply_markdown") applyMarkdown: Boolean = true
    ): FetchMessageResponse

    @FormUrlEncoded
    @PATCH("messages/{message_id}")
    suspend fun changeMessageTopic(
        @Path(value = "message_id", encoded = true) messageId: Int,
        @Field("topic") newTopic: String,
    )
}
