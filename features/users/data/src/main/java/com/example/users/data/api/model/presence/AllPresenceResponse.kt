package com.example.users.data.api.model.presence

import com.google.gson.annotations.SerializedName

class AllPresenceResponse (
    val presences: Map<String, PresenceDto>,
    @SerializedName("server_timestamp") val serverTimestamp: Float
)
