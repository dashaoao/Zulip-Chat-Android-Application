package com.example.users.presentation.people.model

import com.example.users.domain.Presence
import com.example.users.domain.User

data class UserItem(
    val id: String,
    val username: String,
    val icon: String?,
    val mail: String,
    val status: Presence,
)

fun User.toUi() = UserItem(
    id = id,
    username = username,
    icon = icon,
    mail = mail,
    status = status,
)
