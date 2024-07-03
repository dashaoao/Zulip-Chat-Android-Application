package com.example.users.presentation.people

import android.util.Log
import com.example.common.core.result_state.ResultState
import com.example.common.ui.R
import com.example.common.ui.UiText
import com.example.common.util.runCatchingNonCancellation
import com.example.users.domain.GetUsersUseCase
import com.example.users.domain.UsersRepository
import com.example.users.presentation.people.model.PeopleCommand
import com.example.users.presentation.people.model.PeopleEvent
import com.example.users.presentation.people.model.toUi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.coroutines.Actor

class PeopleActor(
    private val peopleNavigation: PeopleNavigation,
    private val usersRepository: UsersRepository,
    private val getUsersUseCase: GetUsersUseCase,
    coroutineScope: CoroutineScope,
) : Actor<PeopleCommand, PeopleEvent> {

    private val searchQueryState: MutableStateFlow<String> = MutableStateFlow("")

    private var getPeopleJob: Job? = null

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, t ->
        Log.e("PeopleActor", t.message.toString())
    }
    private val actorScope = CoroutineScope(coroutineScope.coroutineContext + coroutineExceptionHandler)

    private suspend fun <T> relaunchGetPeopleJob(
        block: suspend () -> T
    ): T {
        getPeopleJob?.cancel()
        val res = actorScope.async { block() }
        getPeopleJob = res
        return res.await()
    }

    override fun execute(command: PeopleCommand): Flow<PeopleEvent> {
        return when (command) {
            PeopleCommand.GetPeople -> getPeople()
            is PeopleCommand.UserClick -> onUserClick(command.userId)
            is PeopleCommand.PerformSearch -> performSearch(command.text)
            PeopleCommand.SubscribeToSearchQueryChanges -> subscribeToSearchQueryChanges()
        }
    }

    private fun getPeople(): Flow<PeopleEvent> = channelFlow {
        relaunchGetPeopleJob {
            runCatchingNonCancellation {
                val usersFlow = getUsersUseCase(searchQueryState.value)
                val ownUserIdFlow = usersRepository.getOwnUserId()

                combine(usersFlow, ownUserIdFlow) { users, ownUserId ->
                    when (ownUserId) {
                        is ResultState.Error -> PeopleEvent.Internal.ErrorLoading(UiText.Resource(R.string.error))
                        is ResultState.Loading -> PeopleEvent.Internal.Loading
                        is ResultState.Success -> {
                            val otherUsers = users.filter { it.id != ownUserId.data }
                            PeopleEvent.Internal.UsersLoaded(data = otherUsers.map { it.toUi() })
                        }
                    }
                }.collectLatest {
                    send(it)
                }
            }.onFailure {
                send(PeopleEvent.Internal.ErrorLoading(UiText.Resource(R.string.error)))
            }
        }
    }

    private fun performSearch(text: String): Flow<PeopleEvent> {
        return flow {
            searchQueryState.value = text
        }
    }

    private fun search(query: String): Flow<PeopleEvent> = channelFlow {
        relaunchGetPeopleJob {
            runCatchingNonCancellation {
                getUsersUseCase(query).collectLatest { result ->
                    send(PeopleEvent.Internal.UsersLoaded(result.map { it.toUi() }))
                }
            }.onFailure {
                send(PeopleEvent.Internal.ErrorLoading(UiText.Resource(R.string.error)))
            }
        }
    }

    private fun subscribeToSearchQueryChanges(): Flow<PeopleEvent> {
        return searchQueryState
            .drop(1)
            .debounce(500L)
            .distinctUntilChanged()
            .filter { it.isEmpty() || it.isNotBlank() }
            .flatMapLatest { search(it) }
    }

    private fun onUserClick(userId: String): Flow<PeopleEvent> {
        peopleNavigation.navigateToProfile(userId)
        return emptyFlow()
    }

}
