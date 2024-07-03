package com.example.common.data

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

class DateTimeConverters {

    @TypeConverter
    fun dateTimeToTimestamp(value: LocalDateTime?): Long? =
        if (value == null)
            null
        else
            ZonedDateTime.of(value, ZoneId.systemDefault()).toInstant().toEpochMilli()

    @TypeConverter
    fun dateTimeFromTimestamp(value: Long?): LocalDateTime? =
        value?.let {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault())
        }

}
