package com.example.users.data.di

import com.example.users.data.UsersRepositoryImpl
import com.example.users.domain.UsersRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface UsersDataModule {
    @Singleton
    @Binds
    fun bindUsersRepository(impl: UsersRepositoryImpl): UsersRepository
}
