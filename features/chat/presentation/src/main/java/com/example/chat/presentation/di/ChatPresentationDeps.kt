package com.example.chat.presentation.di

import android.app.Application
import com.example.chat.domain.MessageRepository
import com.example.chat.presentation.ChatNavigation
import com.example.users.domain.UsersRepository

interface ChatPresentationDeps {
    fun application(): Application
    fun chatNavigation(): ChatNavigation
//    fun messageEventHandler(): MessageEventHandler
    fun usersRepository(): UsersRepository
    fun messageRepository(): MessageRepository
}
