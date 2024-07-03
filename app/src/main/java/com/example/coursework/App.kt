package com.example.coursework

import android.app.Application
import android.content.Context
import com.example.channels.presentation.di.ChannelsDepsStore
import com.example.channels.presentation.new_channel.di.NewChannelDepsStore
import com.example.chat.presentation.channel_topics.di.ChannelTopicsDepsStore
import com.example.chat.presentation.di.ChatDepsStore
import com.example.coursework.di.app.AppComponent
import com.example.coursework.di.app.DaggerAppComponent
import com.example.users.presentation.di.UsersDepsStore

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this@App)
        UsersDepsStore.deps = appComponent
        ChannelsDepsStore.deps = appComponent
        ChatDepsStore.deps = appComponent
        NewChannelDepsStore.deps = appComponent
        ChannelTopicsDepsStore.deps = appComponent
    }
}

fun Context.getAppComponent(): AppComponent = (this.applicationContext as App).appComponent
