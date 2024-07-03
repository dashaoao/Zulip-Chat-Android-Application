package com.example.channels.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.channels.data.database.model.ChannelEntity
import com.example.channels.data.database.model.SubscribedChannelEntity
import com.example.channels.data.database.model.TopicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelsDao {
    @Query("SELECT * FROM channels")
    fun getChannels(): Flow<List<ChannelEntity>>

    @Query("SELECT * FROM subscribed_channels")
    fun getSubscribedChannels(): Flow<List<SubscribedChannelEntity>>

    @Query("SELECT * FROM channels WHERE id=:channelId LIMIT 1")
    fun getChannel(channelId: String): Flow<ChannelEntity>

    @Query("SELECT * FROM channels WHERE id=:channelId LIMIT 1")
    fun getSubscribedChannel(channelId: String): Flow<SubscribedChannelEntity>

    @Transaction
    suspend fun updateChannels(channels: List<ChannelEntity>) {
        clearChannels()
        saveChannels(channels)
    }

    @Transaction
    suspend fun updateSubscribedChannels(channels: List<SubscribedChannelEntity>) {
        clearSubscribedChannels()
        saveSubscribedChannels(channels)
    }

    @Query("DELETE FROM channels")
    suspend fun clearChannels()

    @Query("DELETE FROM subscribed_channels")
    suspend fun clearSubscribedChannels()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveChannels(channels: List<ChannelEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSubscribedChannels(channels: List<SubscribedChannelEntity>)

    @Query("SELECT * FROM topics WHERE channel_id=:channelId")
    fun getTopics(channelId: String): Flow<List<TopicEntity>>

    @Transaction
    suspend fun updateTopics(topics: List<TopicEntity>, channelId: String) {
        clearTopics(channelId)
        saveTopics(topics)
    }

    @Query("DELETE FROM topics WHERE channel_id = :channelId")
    suspend fun clearTopics(channelId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTopics(topics: List<TopicEntity>)
}
