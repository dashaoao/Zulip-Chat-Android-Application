package com.example.users.data.api.model

import com.example.users.data.database.UserEntity
import com.google.gson.annotations.SerializedName

class UserDto (
    @SerializedName("user_id") val id: Int,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("email") val mail: String,
    @SerializedName("is_active") val isActive: Boolean,
    @SerializedName("is_bot") val isBot: Boolean
)

fun UserDto.toDomain(status: com.example.users.domain.Presence) = com.example.users.domain.User(
    id = id.toString(),
    username = fullName,
    icon = avatarUrl,
    mail = mail,
    status = status,
)

fun UserDto.toEntity(status: com.example.users.domain.Presence) = UserEntity(
    id = id,
    name = fullName,
    avatarUrl = avatarUrl,
    email = mail,
    presence = status,
)
