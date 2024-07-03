package com.example.users.domain

import com.example.common.core.result_state.ResultFlow
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    suspend fun getUsers(): Flow<List<User>>
    fun getUserById(idUser: String): ResultFlow<User>
    fun getOwnUser(): ResultFlow<User>
    fun getOwnUserId(): ResultFlow<String>
    fun cleanAuthData(): ResultFlow<Unit>
}
