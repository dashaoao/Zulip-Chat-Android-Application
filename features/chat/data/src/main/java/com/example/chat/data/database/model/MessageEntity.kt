package com.example.chat.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.chat.domain.Message
import com.example.common.core.TopicId
import com.example.common.data.DateTimeConverters
import com.example.common.data.TopicIdConverters
import java.time.LocalDateTime

@Entity(tableName = "message")
@TypeConverters(DateTimeConverters::class, TopicIdConverters::class)
class MessageEntity(
    @PrimaryKey val id : Int,
    @ColumnInfo(name = "topic_id") val topicId: TopicId,
    @ColumnInfo("sender_id") val senderId: String,
    @ColumnInfo("sender_full_name") val senderFullName: String,
    @ColumnInfo("avatar_url") val avatarUrl: String?,
    @ColumnInfo("date_time") val dateTime: LocalDateTime,
    @ColumnInfo("text") val text: String,
)

fun Message.toEntity(topicId: TopicId) = MessageEntity(
    id = id.toInt(),
    topicId = topicId,
    senderId = userId,
    senderFullName = name,
    text = message,
    avatarUrl = icon,
    dateTime = dateTime
)
