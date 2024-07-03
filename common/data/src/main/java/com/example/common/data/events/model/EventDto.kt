package com.example.common.data.events.model

import com.google.gson.annotations.SerializedName

class EventDto(
    @SerializedName("id") val id: Int,
    @SerializedName("type") val type: String,
)
