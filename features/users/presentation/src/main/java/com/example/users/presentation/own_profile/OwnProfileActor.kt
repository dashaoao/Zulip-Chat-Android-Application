package com.example.users.presentation.own_profile

import com.example.common.ui.R
import com.example.common.core.result_state.ResultState
import com.example.common.ui.UiText
import com.example.users.domain.UsersRepository
import com.example.users.presentation.own_profile.model.OwnProfileEvent
import com.example.users.presentation.people.model.toUi
import com.example.users.presentation.own_profile.model.OwnProfileCommand
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vivid.money.elmslie.coroutines.Actor

class OwnProfileActor(
    private val usersRepository: UsersRepository,
) : Actor<OwnProfileCommand, OwnProfileEvent> {

    override fun execute(command: OwnProfileCommand): Flow<OwnProfileEvent> {
        return when (command) {
            OwnProfileCommand.SetOwnProfileInfo -> setOwnProfileInfo()
        }
    }

    private fun setOwnProfileInfo(): Flow<OwnProfileEvent> {
        return usersRepository.getOwnUser().map { ownUser ->
            when (ownUser) {
                is ResultState.Error -> {
                    OwnProfileEvent.Internal.ErrorLoading(UiText.Resource(R.string.error))
                }

                is ResultState.Loading -> {
                    OwnProfileEvent.Internal.Loading
                }

                is ResultState.Success -> {
                    OwnProfileEvent.Internal.OwnProfileInfoLoaded(
                        data = ownUser.data.toUi()
                    )
                }
            }
        }
    }
}
