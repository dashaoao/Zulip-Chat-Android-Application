package com.example.common.data

import androidx.room.TypeConverter
import com.example.common.core.TopicId
import com.google.gson.Gson

class TopicIdConverters {

    @TypeConverter
    fun topicIdToString(value: TopicId): String = Gson().toJson(value)

    @TypeConverter
    fun stringToTopicId(value: String): TopicId = Gson().fromJson(value, TopicId::class.java)
}
