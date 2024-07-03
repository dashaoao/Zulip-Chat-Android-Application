package com.example.users.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        private var INSTANCE : UserDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "users"

        fun getInstance(application: Application) : UserDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK){
                INSTANCE?.let {
                    return it
                }
                val db = Room
                    .databaseBuilder(
                        application,
                        UserDatabase::class.java,
                        DB_NAME
                    )
                    .build()

                INSTANCE = db
                return db
            }
        }
    }
}
