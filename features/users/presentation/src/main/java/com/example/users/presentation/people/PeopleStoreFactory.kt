package com.example.users.presentation.people

import com.example.users.domain.GetUsersUseCase
import com.example.users.domain.UsersRepository
import com.example.users.presentation.people.model.PeopleCommand
import com.example.users.presentation.people.model.PeopleEffect
import com.example.users.presentation.people.model.PeopleEvent
import com.example.users.presentation.people.model.PeopleUIState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import vivid.money.elmslie.coroutines.ElmStoreCompat

class PeopleStoreFactory @AssistedInject constructor(
    @Assisted("coroutineScope") private val coroutineScope: CoroutineScope,
    private val peopleNavigation: PeopleNavigation,
    private val usersRepository: UsersRepository,
    private val getUsersUseCase: GetUsersUseCase,
) {

    @Suppress("UNCHECKED_CAST")
    fun create() = ElmStoreCompat(
        initialState = PeopleUIState(),
        reducer = PeopleReducer,
        actor = PeopleActor(
            peopleNavigation= peopleNavigation,
            usersRepository = usersRepository,
            getUsersUseCase = getUsersUseCase,
            coroutineScope = coroutineScope,
        )
    ) as ElmStoreCompat<PeopleEvent, PeopleUIState, PeopleEffect, PeopleCommand>

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("coroutineScope") coroutineScope: CoroutineScope,
        ) : PeopleStoreFactory
    }
}
