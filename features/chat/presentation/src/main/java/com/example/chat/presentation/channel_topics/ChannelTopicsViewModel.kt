package com.example.chat.presentation.channel_topics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.channels.domain.usecase.GetChannelTopicsUseCase
import com.example.channels.presentation.model.toUI
import com.example.chat.presentation.channel_topics.model.ChannelTopicsUiState
import com.example.common.ui.R
import com.example.common.ui.TopicId
import com.example.common.ui.UiText
import com.example.common.ui.toDomain
import com.example.common.util.runCatchingNonCancellation
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChannelTopicsViewModel @Inject constructor(
    private val getChannelTopicsUseCase: GetChannelTopicsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChannelTopicsUiState())
    val uiState: StateFlow<ChannelTopicsUiState> = _uiState.asStateFlow()

    private val _error = Channel<UiText>()
    val error: Flow<UiText> = _error.receiveAsFlow()

    fun getChannelTopics(topicId: TopicId) = viewModelScope.launch {
        runCatchingNonCancellation {
            getChannelTopicsUseCase.invoke(topicId.toDomain()).collectLatest { topics ->
                _uiState.update { state ->
                    state.copy(
                        topics = topics.map { it.toUI() }
                    )
                }
            }
        }.onFailure {
            _error.send(UiText.Resource(R.string.error))
        }
    }
}
