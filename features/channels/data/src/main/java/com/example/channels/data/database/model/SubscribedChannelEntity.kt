package com.example.channels.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.channels.domain.Channel
import com.example.common.data.DateTimeConverters

@Entity(tableName = "subscribed_channels")
@TypeConverters(DateTimeConverters::class)
class SubscribedChannelEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
)

fun SubscribedChannelEntity.toDomain() = Channel(
    id = id.toString(),
    title = name,
    topics = emptyList()
)

fun Channel.toSubscribedChannelEntity() = SubscribedChannelEntity(
    id = id.toInt(),
    name = title,
)
