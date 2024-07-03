package com.example.users.presentation.people.model

import com.example.common.ui.UiText

data class PeopleUIState(
    val users: List<UserItem> = emptyList(),
    val isLoading: Boolean = false,
)

sealed interface PeopleEvent {
    sealed interface UI : PeopleEvent {
        data object Initialize : UI
        class UserClick(val contactId: String) : UI
        class SearchTextChanged(val text: String) : UI
    }

    sealed interface Internal : PeopleEvent {
        data object Loading : Internal
        class UsersLoaded(val data: List<UserItem>) : Internal
        class ErrorLoading(val msg: UiText) : Internal
    }
}

sealed class PeopleEffect {
    class Error(val msg: UiText) : PeopleEffect()
}

sealed class PeopleCommand {
    data object GetPeople : PeopleCommand()
    data object SubscribeToSearchQueryChanges : PeopleCommand()
    class PerformSearch(val text: String) : PeopleCommand()
    class UserClick(val userId: String) : PeopleCommand()
}
