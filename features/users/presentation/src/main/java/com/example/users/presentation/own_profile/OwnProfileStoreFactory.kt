package com.example.users.presentation.own_profile

import com.example.users.domain.UsersRepository
import com.example.users.presentation.own_profile.model.OwnProfileUIState
import vivid.money.elmslie.coroutines.ElmStoreCompat
import javax.inject.Inject

class OwnProfileStoreFactory @Inject constructor(
    private val usersRepository: UsersRepository,
) {
    fun create() = ElmStoreCompat(
        initialState = OwnProfileUIState(),
        reducer = OwnProfileReducer,
        actor = OwnProfileActor(
            usersRepository = usersRepository,
        )
    )
}
