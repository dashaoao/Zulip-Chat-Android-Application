package com.example.channels.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.channels.data.database.model.ChannelEntity
import com.example.channels.data.database.model.SubscribedChannelEntity
import com.example.channels.data.database.model.TopicEntity

@Database(
    entities = [ChannelEntity::class, SubscribedChannelEntity::class, TopicEntity::class],
    version = 3,
    exportSchema = false
)
abstract class ChannelsDatabase : RoomDatabase() {

    abstract fun channelsDao(): ChannelsDao

    companion object {
        private var INSTANCE: ChannelsDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "channels"

        fun getInstance(application: Application): ChannelsDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = Room
                    .databaseBuilder(
                        application,
                        ChannelsDatabase::class.java,
                        DB_NAME
                    )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = db
                return db
            }
        }
    }
}
