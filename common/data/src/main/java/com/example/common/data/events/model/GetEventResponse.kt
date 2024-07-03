package com.example.common.data.events.model

import com.google.gson.annotations.SerializedName

class GetEventResponse(
    @SerializedName("events") val events: List<EventDto>
)
