package com.example.channels.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.tooling.preview.Preview
import com.example.channels.presentation.model.ChannelEffect
import com.example.channels.presentation.model.ChannelUIState
import com.example.channels.presentation.model.ChannelsItem
import com.example.channels.presentation.new_channel.ui.NewChannelDialog
import com.example.common.ui.CustomSnackbarHost
import com.example.common.ui.TopicId
import com.example.common.ui.theme.ChatTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import vivid.money.elmslie.compose.EffectWithKey

const val topicNameTestTag = "nameTestTag"
const val topicsListContentDesc = "topics list"
const val topicsListTestTag = "topicsListTestTag"

@Composable
fun ChannelsLayout(
    state: ChannelUIState,
    effect: EffectWithKey<ChannelEffect>?,
    performSearch: (text: String) -> Unit,
    onChannelClick: (channelId: String, currentData: List<ChannelsItem>) -> Unit,
    getChannels: (isSubscribed: Boolean) -> Unit,
    onTopicClick: (id: TopicId) -> Unit,
) {
    val systemUiController = rememberSystemUiController()
    val containerColor = MaterialTheme.colorScheme.surface

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    SideEffect {
        systemUiController.setStatusBarColor(
            color = containerColor
        )
    }

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = {
            CustomSnackbarHost(snackbarHostState)
        },
        topBar = {
            ChannelsTopBar(
                performSearch = performSearch,
                onAddClick = { showDialog = true }
            )
        }
    ) { padding ->

        effect?.takeIfInstanceOf<ChannelEffect.Error>()?.key?.let {
            LaunchedEffect(it) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = it.value.msg.stringValue(context),
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }

        SnackbarHost(hostState = snackbarHostState)

        Column(
            modifier = Modifier.padding(padding)
        ) {
            CustomTabRow(
                selectedType = state.type,
                onTabClick = getChannels,
            )

            StreamsScreen(
                items = state.channels,
                onChannelClick = onChannelClick,
                onTopicClick = onTopicClick,
                isLoading = state.isLoading,
            )
        }
    }

    if (showDialog) {
        NewChannelDialog(onDismissRequest = { showDialog = false })
    }
}

fun getTopicItemTestTagById(channelsItem: ChannelsItem): String {
    val itemId = when (channelsItem) {
        is ChannelsItem.Channel -> channelsItem.id
        is ChannelsItem.Topic -> channelsItem.topicId.topicName
    }
    return "channelsItemId=${itemId}"
}

val LazyListItemPosition = SemanticsPropertyKey<Int>("ListItemPosition")
var SemanticsPropertyReceiver.listItemPosition by LazyListItemPosition

@Preview
@Composable
private fun ChannelsScreenPreview() {
    ChatTheme(
        darkTheme = false
    ) {
        ChannelsLayout(
            ChannelUIState(channels = channelsItem),
            effect = null,
            performSearch = {},
            onChannelClick = { _, _ -> },
            getChannels = {},
            onTopicClick = {},
        )
    }
}

private
val channelsItem = listOf(
    ChannelsItem.Channel(
        id = "1",
        title = "#general",
        expanded = true
    ),

    ChannelsItem.Topic(
        topicId = TopicId(
            "1",
            "Testing"
        ),
        title = "Testing",
        messageCount = 1240,
    ),

    ChannelsItem.Topic(
        topicId = TopicId(
            "2",
            "Testing"
        ),
        title = "Bruh",
        messageCount = 24,
    ),

    ChannelsItem.Channel(
        id = "4",
        title = "#Development",
        expanded = false
    ),

    ChannelsItem.Channel(
        id = "5",
        title = "#Design",
        expanded = false
    ),

    ChannelsItem.Channel(
        id = "6",
        title = "#PR",
        expanded = false
    ),
)
