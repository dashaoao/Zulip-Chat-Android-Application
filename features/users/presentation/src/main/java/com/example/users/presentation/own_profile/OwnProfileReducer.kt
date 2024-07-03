package com.example.users.presentation.own_profile

import com.example.users.presentation.own_profile.model.OwnProfileCommand
import com.example.users.presentation.own_profile.model.OwnProfileEffect
import com.example.users.presentation.own_profile.model.OwnProfileEvent
import com.example.users.presentation.own_profile.model.OwnProfileUIState
import vivid.money.elmslie.core.store.Result
import vivid.money.elmslie.core.store.StateReducer

val OwnProfileReducer =
    StateReducer<OwnProfileEvent, OwnProfileUIState, OwnProfileEffect, OwnProfileCommand> { event: OwnProfileEvent, state: OwnProfileUIState ->

        when (event) {
            is OwnProfileEvent.Internal.ErrorLoading -> {
                Result(
                    state = state,
                    effect = OwnProfileEffect.Error(event.msg)
                )
            }

            OwnProfileEvent.Internal.Loading -> {
                Result(
                    state = state.copy(isLoading = true)
                )
            }

            is OwnProfileEvent.Internal.OwnProfileInfoLoaded -> {
                Result(
                    state = state.copy(
                        isLoading = false,
                        username = event.data.username,
                        icon = event.data.icon
                            ?: "https://i.ytimg.com/vi/Mmpi7hq_svk/hqdefault.jpg",
                        mail = event.data.mail,
                        status = event.data.status,
                    )
                )
            }

            OwnProfileEvent.UI.Initialize -> {
                Result(
                    state = OwnProfileUIState(isLoading = true),
                    commands = listOf(
                        OwnProfileCommand.SetOwnProfileInfo,
                    )
                )
            }
        }
    }
