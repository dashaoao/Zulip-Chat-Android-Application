package com.example.users.presentation.di

import android.app.Application
import com.example.users.domain.GetUsersUseCase
import com.example.users.domain.UsersRepository
import com.example.users.presentation.people.PeopleNavigation
import com.example.users.presentation.profile.ProfileNavigation

interface UserPresentationDeps {
    fun application(): Application
    fun peopleNavigation(): PeopleNavigation
    fun profileNavigation(): ProfileNavigation
    fun usersRepository(): UsersRepository
    fun getUsersUseCase(): GetUsersUseCase
}
