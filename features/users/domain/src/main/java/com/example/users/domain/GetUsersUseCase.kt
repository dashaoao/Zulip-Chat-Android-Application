package com.example.users.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetUsersUseCase(
    private val usersRepository: UsersRepository,
) {
    suspend operator fun invoke(query: String): Flow<List<User>> =
        usersRepository.getUsers().map { response ->
            if (query.isEmpty())
                response
            else
                response.performSearch(query)
        }

    private fun List<User>.performSearch(query: String) = this.filter {
        it.username.lowercase().startsWith(query.lowercase())
    }
}
