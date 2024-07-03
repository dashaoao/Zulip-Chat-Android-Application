package com.example.common.data.events.model

import com.google.gson.annotations.SerializedName

class RegisterEventQueueResponse(
    @SerializedName("queue_id") val queueId: String,
    @SerializedName("last_event_id") val lastEventId: Int
)
