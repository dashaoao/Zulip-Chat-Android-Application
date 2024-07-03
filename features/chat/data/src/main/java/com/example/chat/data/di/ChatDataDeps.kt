package com.example.chat.data.di

import com.example.chat.data.api.ChatApi
import com.example.chat.data.database.ChatDao
import com.example.common.data.events.EventsApi
import com.example.users.data.api.UsersApi
import com.example.users.data.database.UserDao

interface ChatDataDeps {
    fun chatApi(): ChatApi
    fun usersApi(): UsersApi
    fun eventsApi(): EventsApi
    fun chatDao(): ChatDao
    fun userDao(): UserDao
}
