package com.example.coursework.di.app

import android.app.Application
import com.example.channels.data.ChannelsRepositoryImpl
import com.example.channels.data.database.ChannelsDao
import com.example.channels.data.di.ChannelsDataDeps
import com.example.channels.data.di.ChannelsDataModule
import com.example.channels.data.di.ChannelsDomainModule
import com.example.channels.domain.usecase.GetChannelTopicsUseCase
import com.example.channels.domain.usecase.GetChannelsUseCase
import com.example.channels.domain.usecase.GetTopicsUseCase
import com.example.channels.presentation.ChannelsNavigation
import com.example.channels.presentation.di.ChannelsPresentationDeps
import com.example.channels.presentation.new_channel.di.NewChannelDeps
import com.example.chat.data.MessageRepositoryImpl
import com.example.chat.data.database.ChatDao
import com.example.chat.data.di.ChatDataDeps
import com.example.chat.data.di.ChatDataModule
import com.example.chat.presentation.ChatNavigation
import com.example.chat.presentation.channel_topics.di.ChannelTopicsDeps
import com.example.chat.presentation.di.ChatPresentationDeps
import com.example.common.data.ApiUrlProvider
import com.example.coursework.MainActivity
import com.example.users.data.UsersRepositoryImpl
import com.example.users.data.database.UserDao
import com.example.users.data.di.UserDataDeps
import com.example.users.data.di.UserDomainModule
import com.example.users.data.di.UsersDataModule
import com.example.users.domain.GetUsersUseCase
import com.example.users.presentation.di.UserPresentationDeps
import com.example.users.presentation.people.PeopleNavigation
import com.example.users.presentation.profile.ProfileNavigation
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppNavigationModule::class, NetworkModule::class,
        AppDataModule::class, ChannelsDataModule::class, ChannelsDomainModule::class,
        UsersDataModule::class, UserDomainModule::class, ChatDataModule::class]
)
interface AppComponent : ChatPresentationDeps, ChatDataDeps,
    UserPresentationDeps, UserDataDeps, ChannelsPresentationDeps,
    ChannelsDataDeps, NewChannelDeps, ChannelTopicsDeps {

    fun inject(activity: MainActivity)
    fun apiUrlProvider(): ApiUrlProvider
    override fun application(): Application
    override fun userDao(): UserDao
    override fun channelsDao(): ChannelsDao
    override fun chatDao(): ChatDao
    override fun channelsNavigation(): ChannelsNavigation
    override fun peopleNavigation(): PeopleNavigation
    override fun chatNavigation(): ChatNavigation
    override fun profileNavigation(): ProfileNavigation
    override fun getChannelsUseCase(): GetChannelsUseCase
    override fun channelsRepository(): ChannelsRepositoryImpl
    override fun getChannelTopicsUseCase(): GetChannelTopicsUseCase
    override fun getTopicsUseCase(): GetTopicsUseCase
    override fun getUsersUseCase(): GetUsersUseCase
    override fun usersRepository(): UsersRepositoryImpl
//    override fun messageEventHandler(): MessageEventHandler
    override fun messageRepository(): MessageRepositoryImpl

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }

}
