package com.example.chat.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.TypeConverters
import com.example.chat.data.database.model.MessageEntity
import com.example.chat.data.database.model.MessageWithReactions
import com.example.chat.data.database.model.ReactionEntity
import com.example.common.core.TopicId
import com.example.common.data.DateTimeConverters
import com.example.common.data.TopicIdConverters

@Dao
interface ChatDao {

    @TypeConverters(DateTimeConverters::class, TopicIdConverters::class)
    @Transaction
    @Query("SELECT * FROM message WHERE topic_id=:topicId")
    suspend fun getMessages(topicId: TopicId): List<MessageWithReactions>

    @Transaction
    suspend fun updateMessages(messages: List<MessageEntity>, reactions: List<ReactionEntity>) {
        clearMessages()
        clearReactions()
        saveMessages(messages)
        saveReactions(reactions)
    }

    @Query("DELETE FROM message")
    suspend fun clearMessages()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMessages(messages: List<MessageEntity>)

    @Query("DELETE FROM reactions")
    suspend fun clearReactions()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveReactions(reactions: List<ReactionEntity>)
}
