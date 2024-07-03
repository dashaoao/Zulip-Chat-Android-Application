package com.example.coursework.di.navbar

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides

@Module
class BottomNavigationModule {

    @BottomNavigationScope
    @Provides
    fun provideCicerone() = Cicerone.create()

    @BottomNavigationScope
    @Provides
    @BottomNavigation
    fun provideRouter(cicerone: Cicerone<Router>) = cicerone.router

    @BottomNavigationScope
    @Provides
    @BottomNavigation
    fun provideNavigatorHolder(cicerone: Cicerone<Router>) = cicerone.getNavigatorHolder()
}
