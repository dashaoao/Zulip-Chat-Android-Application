package com.example.chat.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.chat.data.database.model.MessageEntity
import com.example.chat.data.database.model.ReactionEntity

@Database(
    entities = [MessageEntity::class, ReactionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ChatDatabase  : RoomDatabase() {

    abstract fun chatDao(): ChatDao

    companion object {
        private var INSTANCE: ChatDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "channels"

        fun getInstance(application: Application): ChatDatabase {
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
                        ChatDatabase::class.java,
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
