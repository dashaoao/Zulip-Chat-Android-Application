package com.example.users.data

import android.app.Application
import android.content.Context
import com.example.common.core.result_state.ResultFlow
import com.example.common.core.result_state.flowState
import com.example.common.core.result_state.map
import com.example.common.core.result_state.onSuccess
import com.example.common.core.result_state.success
import com.example.users.data.api.UsersApi
import com.example.users.data.api.model.presence.toDomain
import com.example.users.data.api.model.toDomain
import com.example.users.data.api.model.toEntity
import com.example.users.data.database.UserDao
import com.example.users.data.database.UserEntity
import com.example.users.data.database.toDomain
import com.example.users.domain.Presence
import com.example.users.domain.User
import com.example.users.domain.UsersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class UsersRepositoryImpl @Inject constructor(
    private val usersApi: UsersApi,
    private val userDao: UserDao,
    application: Application,
) : UsersRepository {

    private val sharedPrefs = application.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)

    override suspend fun getUsers(): Flow<List<User>> = getCacheUsers().also {
        CoroutineScope(coroutineContext).launch {
            getFreshUsers()
        }
    }

    private fun getCacheUsers(): Flow<List<User>> =
        userDao.getUsers().map { users -> users.map { it.toDomain() } }.distinctUntilChanged()

    private suspend fun getFreshUsers() {
        val presence: Map<String, Presence> = getAllPresence()
        val freshContacts =
            usersApi.getUsers().users.filter { it.isActive && it.isBot.not() }
                .map {
                    it.toEntity(presence.getOrDefault(it.mail, Presence.OFFLINE))
                }
        userDao.updateUsers(freshContacts)
    }

    private suspend fun getAllPresence(): Map<String, Presence> =
        usersApi.getPresences().let {
            it.presences.mapValues { entry ->
                entry.value.aggregated.toDomain(it.serverTimestamp)
            }
        }

    override fun getUserById(idUser: String): ResultFlow<User> =
        flowState { getFreshUser(idUser).toDomain()}

    private suspend fun getFreshUser(idUser: String): UserEntity {
        val freshUser = usersApi.getUserById(idUser).user.toEntity(
            getUserPresence(idUser)
        )
        userDao.updateUser(freshUser)
        return freshUser
    }

    override fun getOwnUser(): ResultFlow<User> =
        flowState {
            usersApi.getOwnUser().toDomain(
                getUserPresence(usersApi.getOwnUser().id.toString())
            )
        }

    override fun getOwnUserId(): ResultFlow<String> {
        return sharedPrefs.getString(KEY_OWN_USER_ID, null)?.let {
            flowOf(it.success())
        } ?: getOwnUser().map { state ->
            state.onSuccess {
                sharedPrefs.edit().putString(KEY_OWN_USER_ID, it.id).apply()
            }.map { it.id }
        }
    }

    override fun cleanAuthData(): ResultFlow<Unit> {
        sharedPrefs.edit().remove(KEY_OWN_USER_ID).apply()
        return emptyFlow()
    }

    private suspend fun getUserPresence(idUser: String) =
        usersApi.getUserPresence(idUser.toInt()).presence.aggregated.toDomain()

    companion object {
        private const val USER_PREFS = "user_prefs"
        private const val KEY_OWN_USER_ID = "OWN_USER_ID"
    }
}
