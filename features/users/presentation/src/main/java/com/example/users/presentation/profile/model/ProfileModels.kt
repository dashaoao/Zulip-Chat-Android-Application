package com.example.users.presentation.profile.model

import com.example.common.ui.UiText
import com.example.users.domain.Presence
import com.example.users.presentation.people.model.UserItem

data class ProfileUIState(
    val username: String = "",
    val icon: String = "",
    val mail: String = "",
    val status: Presence = Presence.OFFLINE,
    val isLoading: Boolean = false,
)

sealed interface ProfileEvent {
    sealed interface UI : ProfileEvent {
        data object Initialize : UI
        data object OnArrowBackClick : UI
    }

    sealed interface Internal : ProfileEvent {
        data object Loading : Internal
        class ProfileInfoLoaded(val data: UserItem) : Internal
        class ErrorLoading(val msg: UiText) : Internal
    }
}

sealed class ProfileEffect {
    class Error(val msg: UiText) : ProfileEffect()
}

sealed class ProfileCommand {
    data object SetProfileInfo : ProfileCommand()
    data object OnArrowBackClick : ProfileCommand()
}
