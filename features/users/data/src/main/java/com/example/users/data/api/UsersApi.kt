package com.example.users.data.api

import com.example.users.data.api.model.UserDto
import com.example.users.data.api.model.UserResponse
import com.example.users.data.api.model.UsersResponse
import com.example.users.data.api.model.presence.AllPresenceResponse
import com.example.users.data.api.model.presence.PresenceResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface UsersApi {

    @GET("users")
    suspend fun getUsers() : UsersResponse

    @GET("users/{user_id}")
    suspend fun getUserById(
        @Path(value = "user_id", encoded = true) idUser: String,
    ) : UserResponse

    @GET("users/me")
    suspend fun getOwnUser() : UserDto

    @GET("realm/presence")
    suspend fun getPresences() : AllPresenceResponse

    @GET("users/{user_id_or_email}/presence")
    suspend fun getUserPresence(
        @Path(value = "user_id_or_email", encoded = true) userId: Int,
    ) : PresenceResponse

}
