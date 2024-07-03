package com.example.users.presentation.profile

import com.example.users.presentation.profile.model.ProfileCommand
import com.example.users.presentation.profile.model.ProfileEffect
import com.example.users.presentation.profile.model.ProfileEvent
import com.example.users.presentation.profile.model.ProfileUIState
import vivid.money.elmslie.core.store.Result
import vivid.money.elmslie.core.store.StateReducer

val ProfileReducer = StateReducer { event: ProfileEvent, state: ProfileUIState ->
    when (event) {
        is ProfileEvent.Internal.ErrorLoading -> {
            Result(
                state = state,
                effect = ProfileEffect.Error(event.msg)
            )
        }

        ProfileEvent.Internal.Loading -> {
            Result(
                state = state.copy(isLoading = true)
            )
        }

        is ProfileEvent.Internal.ProfileInfoLoaded -> {
            Result(
                state = state.copy(
                    isLoading = false,
                    username = event.data.username,
                    icon = event.data.icon ?: "https://i.ytimg.com/vi/Mmpi7hq_svk/hqdefault.jpg",
                    mail = event.data.mail,
                    status = event.data.status,
                )
            )
        }

        ProfileEvent.UI.Initialize -> {
            Result(
                state = ProfileUIState(isLoading = true),
                command = ProfileCommand.SetProfileInfo,
            )
        }

        is ProfileEvent.UI.OnArrowBackClick -> {
            Result(
                state = state,
                command = ProfileCommand.OnArrowBackClick
            )
        }
    }
}
