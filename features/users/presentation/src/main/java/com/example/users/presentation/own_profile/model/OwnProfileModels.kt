package com.example.users.presentation.own_profile.model

import com.example.common.ui.UiText
import com.example.users.domain.Presence
import com.example.users.presentation.people.model.UserItem

data class OwnProfileUIState(
    val username: String = "",
    val icon: String = "",
    val mail: String = "",
    val status: Presence = Presence.OFFLINE,
    val isLoading: Boolean = false,
)

sealed interface OwnProfileEvent {
    sealed interface UI : OwnProfileEvent {
        data object Initialize : UI
    }

    sealed interface Internal : OwnProfileEvent {
        data object Loading : Internal
        class OwnProfileInfoLoaded(val data: UserItem) : Internal
        class ErrorLoading(val msg: UiText) : Internal
    }
}

sealed class OwnProfileEffect {
    class Error(val msg: UiText) : OwnProfileEffect()
}

sealed interface OwnProfileCommand {
    data object SetOwnProfileInfo : OwnProfileCommand
}
