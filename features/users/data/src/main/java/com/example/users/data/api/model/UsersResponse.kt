package com.example.users.data.api.model

import com.google.gson.annotations.SerializedName

class UsersResponse (
    @SerializedName("members") val users : List<UserDto>
)
