package com.example.channels.presentation

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.example.channels.domain.usecase.GetChannelsUseCase
import com.example.channels.domain.usecase.GetTopicsUseCase
import com.example.channels.presentation.model.ChannelUIState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import vivid.money.elmslie.coroutines.ElmStoreCompat

class ChannelStoreFactory @AssistedInject constructor(
    @Assisted("coroutineScope") private val coroutineScope: CoroutineScope,
    private val getChannelsUseCase: GetChannelsUseCase,
    private val getTopicsUseCase: GetTopicsUseCase,
    private val channelsNavigation: ChannelsNavigation,
) {
    @OptIn(UnstableApi::class)
    fun create() = ElmStoreCompat(
        initialState = ChannelUIState(),
        reducer = ChannelReducer(),
        actor = ChannelsActor(
            coroutineScope = coroutineScope,
            channelsNavigation = channelsNavigation,
            getChannelsUseCase = getChannelsUseCase,
            getTopicsUseCase = getTopicsUseCase,
        )
    )

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("coroutineScope") coroutineScope: CoroutineScope,
        ) : ChannelStoreFactory
    }
}
