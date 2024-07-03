package com.example.users.presentation.people

import com.example.users.presentation.people.model.PeopleCommand
import com.example.users.presentation.people.model.PeopleEffect
import com.example.users.presentation.people.model.PeopleEvent
import com.example.users.presentation.people.model.PeopleUIState
import vivid.money.elmslie.core.store.Result
import vivid.money.elmslie.core.store.StateReducer

val PeopleReducer = StateReducer { event: PeopleEvent, state: PeopleUIState ->
    when (event) {
        is PeopleEvent.UI.Initialize -> {
            Result(
                state = PeopleUIState(isLoading = true),
                commands = listOf(
                    PeopleCommand.SubscribeToSearchQueryChanges,
                    PeopleCommand.GetPeople,
                )
            )
        }

        is PeopleEvent.UI.UserClick -> {
            Result(
                state = state,
                command = PeopleCommand.UserClick(event.contactId)
            )
        }

        is PeopleEvent.Internal.UsersLoaded -> {
            Result(
                state = state.copy(users = event.data, isLoading = false)
            )
        }

        is PeopleEvent.Internal.ErrorLoading -> {
            Result(
                state = state,
                effect = PeopleEffect.Error(event.msg)
            )
        }

        PeopleEvent.Internal.Loading -> {
            Result(
                state = state.copy(isLoading = true)
            )
        }

        is PeopleEvent.UI.SearchTextChanged -> {
            Result(
                state = state.copy(isLoading = true),
                command = PeopleCommand.PerformSearch(
                    event.text
                )
            )
        }
    }
}
