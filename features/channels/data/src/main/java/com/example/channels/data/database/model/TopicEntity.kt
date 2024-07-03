package com.example.channels.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.channels.domain.Topic
import com.example.common.core.TopicId
import com.example.common.data.TopicIdConverters

@Entity(tableName = "topics")
@TypeConverters(TopicIdConverters::class)
class TopicEntity (
    @PrimaryKey val id: TopicId,
    @ColumnInfo(name = "channel_id") val channelId: String,
    @ColumnInfo(name = "name") val name: String,
)

fun TopicEntity.toDomain(channelId: String) = Topic(
    id = TopicId(channelId, id.topicName),
    title = name,
)

fun Topic.toEntity() = TopicEntity(
    id = id,
    channelId = id.streamId,
    name = id.topicName,
)
