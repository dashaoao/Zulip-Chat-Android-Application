package com.example.users.presentation.profile

import com.example.common.core.result_state.ResultState
import com.example.common.ui.R
import com.example.common.ui.UiText
import com.example.common.util.catchNonCancellationAndEmit
import com.example.users.domain.UsersRepository
import com.example.users.presentation.people.model.toUi
import com.example.users.presentation.profile.model.ProfileCommand
import com.example.users.presentation.profile.model.ProfileEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import vivid.money.elmslie.coroutines.Actor

class ProfileActor(
    private val usersRepository: UsersRepository,
    private val userId: String?,
    private val profileNavigation: ProfileNavigation,
) : Actor<ProfileCommand, ProfileEvent> {
    override fun execute(command: ProfileCommand): Flow<ProfileEvent> {
        return when (command) {
            ProfileCommand.SetProfileInfo -> setProfileInfo()
            is ProfileCommand.OnArrowBackClick -> onArrowBackClick()
        }
    }

    private fun onArrowBackClick(): Flow<ProfileEvent> {
        profileNavigation.exitProfile()
        return emptyFlow()
    }

    private fun setProfileInfo(): Flow<ProfileEvent> {
        return if (userId != null) {
            usersRepository.getUserById(userId).map { result ->
                when(result){
                    is ResultState.Error -> {
                        ProfileEvent.Internal.ErrorLoading(UiText.Resource(R.string.error))
                    }

                    is ResultState.Loading -> {
                        ProfileEvent.Internal.Loading
                    }

                    is ResultState.Success -> {
                        ProfileEvent.Internal.ProfileInfoLoaded(
                            data = result.data.toUi()
                        )
                    }
                }
            }
        } else {
            emptyFlow()
        }.catchNonCancellationAndEmit(ProfileEvent.Internal.ErrorLoading(UiText.Resource(R.string.error)))
    }
}
