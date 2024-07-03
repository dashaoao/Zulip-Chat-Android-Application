package com.example.channels.domain

data class Channel(
    val id: String,
    val title: String,
    val topics: List<Topic>?,
)


