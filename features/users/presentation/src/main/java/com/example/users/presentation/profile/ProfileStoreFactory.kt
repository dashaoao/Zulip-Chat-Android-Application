package com.example.users.presentation.profile

import com.example.users.domain.UsersRepository
import com.example.users.presentation.profile.model.ProfileCommand
import com.example.users.presentation.profile.model.ProfileEffect
import com.example.users.presentation.profile.model.ProfileEvent
import com.example.users.presentation.profile.model.ProfileUIState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import vivid.money.elmslie.coroutines.ElmStoreCompat

class ProfileStoreFactory @AssistedInject constructor(
    private val usersRepository: UsersRepository,
    @Assisted("userId") val userId: String?,
    private val profileNavigation: ProfileNavigation,
) {
    @Suppress("UNCHECKED_CAST")
    fun create() = ElmStoreCompat(
        initialState = ProfileUIState(),
        reducer = ProfileReducer,
        actor = ProfileActor(
            profileNavigation = profileNavigation,
            usersRepository = usersRepository,
            userId = userId
        )
    ) as ElmStoreCompat<ProfileEvent, ProfileUIState, ProfileEffect, ProfileCommand>

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("userId") userId: String?,
        ): ProfileStoreFactory
    }
}
