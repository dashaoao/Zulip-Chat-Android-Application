package com.example.users.domain

data class User(
    val id: String,
    val username: String,
    val icon: String?,
    val mail: String,
    val status: Presence,
)
