package com.example.users.data.di

import com.example.users.domain.GetUsersUseCase
import com.example.users.domain.UsersRepository
import dagger.Module
import dagger.Provides

@Module
class UserDomainModule {

    @Provides
    fun provideGetUsersUseCase(
        usersRepository: UsersRepository
    ): GetUsersUseCase =
        GetUsersUseCase(usersRepository)

}
