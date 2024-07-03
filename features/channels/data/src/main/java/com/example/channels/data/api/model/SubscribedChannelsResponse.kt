package com.example.channels.data.api.model

import com.google.gson.annotations.SerializedName

class SubscribedChannelsResponse (
    @SerializedName("subscriptions") val streams: List<StreamDto>
)
