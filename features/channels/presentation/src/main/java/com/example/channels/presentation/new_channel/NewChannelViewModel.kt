package com.example.channels.presentation.new_channel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.channels.domain.ChannelsRepository
import com.example.channels.presentation.new_channel.model.NewChannelNavEvent
import com.example.channels.presentation.new_channel.model.NewChannelUiState
import com.example.common.core.result_state.onError
import com.example.common.core.result_state.onSuccess
import com.example.common.ui.R
import com.example.common.ui.UiText
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

class NewChannelViewModel @Inject constructor(
    private val channelsRepository: ChannelsRepository,
) : ViewModel() {

    private val _error = Channel<UiText>()
    val error: Flow<UiText> = _error.receiveAsFlow()

    private val _uiState = MutableStateFlow(NewChannelUiState())
    val uiState: StateFlow<NewChannelUiState> = _uiState.asStateFlow()

    private val _navEvent = Channel<NewChannelNavEvent>()
    val navEvent: Flow<NewChannelNavEvent> = _navEvent.receiveAsFlow()

    fun addNewChannel() = viewModelScope.launch {
        runCatchingNonCancellation {
            channelsRepository.addNewChannel(uiState.value.text).collectLatest { result ->
                result.onSuccess {
                    _navEvent.send(NewChannelNavEvent.Exit)
                    _uiState.update { state ->
                        state.copy(text = "")
                    }
                }.onError { _, _ ->
                    _error.send(UiText.Resource(R.string.error))
                }
            }
        }.onFailure {
            _error.send(UiText.Resource(R.string.error))
        }
    }

    fun onChannelNameChanged(text: String) {
        _uiState.update {
            it.copy(text = text)
        }
    }
}
