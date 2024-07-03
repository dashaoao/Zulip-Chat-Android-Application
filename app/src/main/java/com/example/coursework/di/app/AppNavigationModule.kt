package com.example.coursework.di.app

import com.example.channels.presentation.ChannelsNavigation
import com.example.chat.presentation.ChatNavigation
import com.example.coursework.AppRouter
import com.example.users.presentation.people.PeopleNavigation
import com.example.users.presentation.profile.ProfileNavigation
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppNavigationModule {

    @Singleton
    @Provides
    fun provideCicerone(): Cicerone<AppRouter> = Cicerone.create(AppRouter())

    @Singleton
    @Provides
    fun provideRouter(cicerone: Cicerone<AppRouter>): Router = cicerone.router

    @Singleton
    @Provides
    fun provideNavigatorHolder(cicerone: Cicerone<AppRouter>) = cicerone.getNavigatorHolder()

    @Singleton
    @Provides
    fun provideChannelsNavigation(cicerone: Cicerone<AppRouter>): ChannelsNavigation = cicerone.router

    @Singleton
    @Provides
    fun providePeopleNavigation(cicerone: Cicerone<AppRouter>): PeopleNavigation = cicerone.router

    @Singleton
    @Provides
    fun provideChatNavigation(cicerone: Cicerone<AppRouter>): ChatNavigation = cicerone.router

    @Singleton
    @Provides
    fun provideProfileNavigation(cicerone: Cicerone<AppRouter>): ProfileNavigation = cicerone.router
}
