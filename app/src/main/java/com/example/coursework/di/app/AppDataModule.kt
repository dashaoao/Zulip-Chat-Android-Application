package com.example.coursework.di.app

import android.app.Application
import com.example.channels.data.database.ChannelsDao
import com.example.channels.data.database.ChannelsDatabase
import com.example.chat.data.database.ChatDao
import com.example.chat.data.database.ChatDatabase
import com.example.users.data.database.UserDao
import com.example.users.data.database.UserDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppDataModule {
    @Singleton
    @Provides
    fun provideUserDatabase(application: Application): UserDatabase =
        UserDatabase.getInstance(application)

    @Singleton
    @Provides
    fun provideUserDao(database: UserDatabase): UserDao = database.userDao()

    @Singleton
    @Provides
    fun provideChannelsDatabase(application: Application): ChannelsDatabase =
        ChannelsDatabase.getInstance(application)

    @Singleton
    @Provides
    fun provideChannelsDao(database: ChannelsDatabase): ChannelsDao = database.channelsDao()

    @Singleton
    @Provides
    fun provideDatabase(application: Application):ChatDatabase =
       ChatDatabase.getInstance(application)

    @Singleton
    @Provides
    fun provideChatDao(database: ChatDatabase): ChatDao = database.chatDao()
}
