package com.example.chat.data.api.model

import com.google.gson.annotations.SerializedName

class FetchMessageResponse (
    @SerializedName("message") val message: MessageDto
)
