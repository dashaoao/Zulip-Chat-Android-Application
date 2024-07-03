package com.example.channels.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.channels.domain.Channel
import com.example.common.data.TopicIdConverters

@Entity(tableName = "channels")
@TypeConverters(TopicIdConverters::class)
class ChannelEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
)

fun ChannelEntity.toDomain() = Channel(
    id = id.toString(),
    title = name,
    topics = emptyList()
)
