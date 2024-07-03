package com.example.channels.presentation

import android.util.Log
import com.example.channels.domain.usecase.GetChannelsUseCase
import com.example.channels.domain.usecase.GetTopicsUseCase
import com.example.channels.presentation.model.ChannelCommand
import com.example.channels.presentation.model.ChannelEvent
import com.example.channels.presentation.model.SearchParams
import com.example.channels.presentation.model.toUI
import com.example.common.ui.R
import com.example.common.ui.TopicId
import com.example.common.ui.UiText
import com.example.common.util.runCatchingNonCancellation
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import vivid.money.elmslie.coroutines.Actor

class ChannelsActor(
    private val getChannelsUseCase: GetChannelsUseCase,
    private val getTopicsUseCase: GetTopicsUseCase,
    private val channelsNavigation: ChannelsNavigation,
    coroutineScope: CoroutineScope,
) : Actor<ChannelCommand, ChannelEvent> {

    private val searchQueryState: MutableStateFlow<SearchParams> = MutableStateFlow(SearchParams())

    private var getChannelsJob: Job? = null
    private var getTopicsJob: Job? = null

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, t ->
        Log.e("ChannelsActor", t.message.toString())
    }

    private val actorScope =
        CoroutineScope(coroutineScope.coroutineContext + coroutineExceptionHandler + SupervisorJob())

    override fun execute(command: ChannelCommand): Flow<ChannelEvent> {
        return when (command) {
            is ChannelCommand.GetChannels -> {
                getChannels(command.isSubscribed)
            }

            is ChannelCommand.NavigateToTopicChat -> {
                navigateToTopicChat(command.topicId)
            }

            is ChannelCommand.PerformSearch -> {
                performSearch(command.text, command.isSubscribed)
            }

            ChannelCommand.SubscribeToSearchQueryState -> {
                subscribeToSearchQueryChanges()
            }

            is ChannelCommand.GetChannelTopics -> {
                loadChannelTopics(command.channelId)
            }
        }
    }

    private suspend fun relaunchGetChannelsJob(
        block: suspend CoroutineScope.() -> Unit
    ) {
        getChannelsJob?.cancel()
        val res = actorScope.async { block() }
        getChannelsJob = res
        res.await()
    }

    private fun getChannels(isSubscribed: Boolean): Flow<ChannelEvent> = channelFlow {
        relaunchGetChannelsJob {
            runCatchingNonCancellation {
                getChannelsUseCase(searchQueryState.value.query, isSubscribed, emptySet()).collectLatest { result ->
                    send(ChannelEvent.Internal.ChannelsLoaded(result.map { it.toUI() }))
                }
            }.onFailure {
                onFailure()
            }
        }
    }

    private fun performSearch(text: String, isSubscribed: Boolean): Flow<ChannelEvent> {
        return flow {
            searchQueryState.update {
                SearchParams(
                    query = text,
                    subscribed = isSubscribed
                )
            }
        }
    }

    private fun search(query: String, isSubscribed: Boolean): Flow<ChannelEvent> = channelFlow {
        relaunchGetChannelsJob {
            runCatchingNonCancellation {
                getChannelsUseCase(query, isSubscribed, emptySet()).collectLatest { result ->
                    send(ChannelEvent.Internal.ChannelsLoaded(result.map { it.toUI() }))
                }
            }.onFailure {
                onFailure()
            }
        }
    }

    private fun subscribeToSearchQueryChanges(): Flow<ChannelEvent> {
        return searchQueryState
            .drop(1)
            .debounce(500L)
            .distinctUntilChanged()
            .filter { it.query.isEmpty() || it.query.isNotBlank() }
            .flatMapLatest { search(it.query, it.subscribed) }
    }

    private fun loadChannelTopics(channelId: String): Flow<ChannelEvent> = channelFlow {
        runCatchingNonCancellation {
            getTopicsJob?.cancel()
            val res = actorScope.async {
                runCatchingNonCancellation {
                    getTopicsUseCase(
                        channelId = channelId,
                        needRefresh = true,
                    ).collectLatest { result ->
                        val mapped = result.map { it.toUI() }
                        send(ChannelEvent.Internal.TopicsLoaded(channelId, mapped))
                    }
                }.onFailure {
                    onFailure()
                }
            }
            getTopicsJob = res
            res.await()
        }
    }

    private fun navigateToTopicChat(topicId: TopicId): Flow<ChannelEvent> {
        channelsNavigation.navigateToChat(topicId)
        return emptyFlow()
    }

    private suspend fun ProducerScope<ChannelEvent>.onFailure() {
        send(
            ChannelEvent.Internal.ErrorLoading(UiText.Resource(R.string.error))
        )
    }
}
