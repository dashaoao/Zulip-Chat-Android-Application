package com.example.channels.presentation.di

import android.app.Application
import com.example.channels.domain.usecase.GetChannelsUseCase
import com.example.channels.domain.usecase.GetTopicsUseCase
import com.example.channels.presentation.ChannelsNavigation

interface ChannelsPresentationDeps {
    fun application(): Application
    fun channelsNavigation(): ChannelsNavigation
    fun getChannelsUseCase(): GetChannelsUseCase
    fun getTopicsUseCase(): GetTopicsUseCase
}
