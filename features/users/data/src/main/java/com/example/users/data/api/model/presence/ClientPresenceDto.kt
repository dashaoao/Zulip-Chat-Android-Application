package com.example.users.data.api.model.presence

import com.example.users.domain.Presence

class ClientPresenceDto (
    val status: String,
    val timestamp: Int
)

fun ClientPresenceDto.toDomain(): Presence = when (status) {
    "idle" -> Presence.IDLE
    "active" -> Presence.ACTIVE
    else -> Presence.OFFLINE
}

fun ClientPresenceDto.toDomain(serverTimestamp: Float): Presence =
    if (serverTimestamp - timestamp > 60 * 10) // FIXME сделать нормально
        Presence.OFFLINE
    else when (status) {
        "idle" -> Presence.IDLE
        "active" -> Presence.ACTIVE
        else -> throw IllegalArgumentException()
    }

