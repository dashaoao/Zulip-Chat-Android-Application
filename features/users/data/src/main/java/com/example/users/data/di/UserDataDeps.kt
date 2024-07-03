package com.example.users.data.di

import com.example.users.data.api.UsersApi
import com.example.users.data.database.UserDao
import com.example.users.domain.GetUsersUseCase
import com.example.users.domain.UsersRepository

interface UserDataDeps {
    fun usersApi(): UsersApi
    fun userDao(): UserDao
}
