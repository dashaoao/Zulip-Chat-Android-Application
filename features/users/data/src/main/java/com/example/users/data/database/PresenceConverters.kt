package com.example.users.data.database

import androidx.room.TypeConverter
import com.example.users.domain.Presence

class PresenceConverters {
    @TypeConverter
    fun presenceToString(value: Presence): String =
        when (value) {
            Presence.ACTIVE -> "ACTIVE"
            Presence.IDLE -> "IDLE"
            Presence.OFFLINE -> "OFFLINE"
        }

    @TypeConverter
    fun stringToPresence(value: String): Presence =
        when (value) {
            "ACTIVE" -> Presence.ACTIVE
            "IDLE" -> Presence.IDLE
            else -> Presence.OFFLINE
        }
}
