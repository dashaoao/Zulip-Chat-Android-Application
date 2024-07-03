package com.example.chat.presentation.ui

import android.text.Html
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chat.presentation.ChatFragment.Companion.chatItemTestTag
import com.example.chat.presentation.ChatFragment.Companion.messageListTestTag
import com.example.chat.presentation.ChatFragment.Companion.reactionListTestTag
import com.example.chat.presentation.ChatFragment.Companion.titleTestTag
import com.example.chat.presentation.channel_topics.ui.ChannelTopicsBottomSheet
import com.example.chat.presentation.model.ChatEffect
import com.example.chat.presentation.model.ChatItem
import com.example.chat.presentation.model.ChatUiState
import com.example.chat.presentation.model.DialogState
import com.example.chat.presentation.ui.custom_message.model.EmojiNCSUi
import com.example.chat.presentation.ui.custom_message.model.ReactionUi
import com.example.common.ui.CustomSnackbarHost
import com.example.common.ui.TopicId
import com.example.common.ui.theme.ChatTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import vivid.money.elmslie.compose.EffectWithKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatLayout(
    state: ChatUiState,
    effect: EffectWithKey<ChatEffect>?,
    onArrowBackClick: () -> Unit,
    onTextChange: (String) -> Unit,
    onClick: () -> Unit,
    onLongClick: (idMessage: String, isOwnMessage: Boolean) -> Unit,
    onAddClick: (idMessage: String) -> Unit,
    onDismissRequest: () -> Unit,
    onEmojiClick: (EmojiNCSUi) -> Unit,
    onReactionClick: (idMessage: String, reactionUI: ReactionUi) -> Unit,
    onCopyButtonClick: () -> Unit,
    onDeleteButtonClick: () -> Unit,
    onEditButtonClick: () -> Unit,
    onChangeTopicClick: () -> Unit,
    onTopicSelected: (topicId: TopicId) -> Unit,
    onLastVisibleItemChange: (index: Int) -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val chatListState = rememberLazyListState()

    ScrollDownOnNewMessage(key = state.chatItems, listState = chatListState)

    LaunchedEffect(chatListState) {
        snapshotFlow {
            chatListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }.collectLatest {
            it?.let { onLastVisibleItemChange(it) }
        }
    }

    Scaffold(
        snackbarHost = {
            CustomSnackbarHost(snackbarHostState)
        },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { onArrowBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(text = "Chat", modifier = Modifier.semantics {
                        testTag = titleTestTag
                    })
                })
        },
        bottomBar = {
            MessageBar(
                text = state.message,
                onTextChange = onTextChange,
                placeholder = "Написать...",
                onClick = onClick,
                buttonState = state.buttonState,
                editingMessage = state.editingMessage,
            )
        }
    ) { padding ->

        effect?.takeIfInstanceOf<ChatEffect.Error>()?.key?.let {
            LaunchedEffect(it) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = it.value.msg.stringValue(context),
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }

        effect?.takeIfInstanceOf<ChatEffect.Copied>()?.key?.let {
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

        when (state.dialogState) {
            is DialogState.ActionBar -> {
                ActionBar(
                    onEmojiClick,
                    onDismissRequest,
                    emojiList = state.dialogState.emojiList,
                    onCopyButtonClick = onCopyButtonClick,
                    onDeleteButtonClick = onDeleteButtonClick,
                    onEditButtonClick = onEditButtonClick,
                    modifier = Modifier.semantics {
                        contentDescription = reactionListTestTag
                        testTag = reactionListTestTag
                    },
                )
            }

            is DialogState.EmojiPick -> {
                EmojiPickBottomSheet(
                    onEmojiClick,
                    onDismissRequest,
                    state.dialogState.emojiList,
                    modifier = Modifier.semantics {
                        contentDescription = reactionListTestTag
                        testTag = reactionListTestTag
                    },
                )
            }

            is DialogState.OwnActionBar -> {
                OwnActionBar(
                    onEmojiClick,
                    onDismissRequest,
                    emojiList = state.dialogState.emojiList,
                    onCopyButtonClick = onCopyButtonClick,
                    onDeleteButtonClick = onDeleteButtonClick,
                    onEditButtonClick = onEditButtonClick,
                    onChangeTopicClick = onChangeTopicClick,
                    modifier = Modifier.semantics {
                        contentDescription = reactionListTestTag
                        testTag = reactionListTestTag
                    },
                )
            }

            DialogState.NoDialog -> {}
            is DialogState.TopicBar -> {
                ChannelTopicsBottomSheet(
                    topicId = state.dialogState.topicId,
                    onTopicSelected = onTopicSelected,
                    onDismissRequest
                )
            }
        }

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 4.dp,
                )
            }
        } else {

            LazyColumn(
                state = chatListState,
                modifier = Modifier
                    .padding(padding)
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = messageListTestTag
                        testTag = messageListTestTag
                    },
                reverseLayout = true,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 8.dp
                )
            ) {
                items(items = state.chatItems, key = { item -> item.id }) { item ->
                    when (item) {
                        is ChatItem.Date ->
                            DateChatItem(
                                date = item.date,
                                modifier = Modifier.semantics {
                                    testTag = chatItemTestTag
                                }
                            )

                        is ChatItem.Message ->
                            MessageChatItem(
                                icon = item.icon
                                    ?: "https://i.ytimg.com/vi/Mmpi7hq_svk/hqdefault.jpg",
                                name = item.name,
                                message = Html.fromHtml(item.message).toString().trim(),
                                reactions = item.reactions,
                                onAddClick = { onAddClick(item.id) },
                                onReactionClick = { onReactionClick(item.id, it) },
                                onActionClick = { onLongClick(item.id, false) },
                                modifier = Modifier.semantics {
                                    testTag = chatItemTestTag
                                    contentDescription =
                                        Html.fromHtml(item.message).toString()
                                            .trim() + "?count=" + item.reactions.size.toString()
                                }
                            )

                        is ChatItem.OwnMessage ->
                            OwnMessageChatItem(
                                message = Html.fromHtml(item.message).toString().trim(),
                                reactions = item.reactions,
                                onAddClick = { onAddClick(item.id) },
                                onReactionClick = { onReactionClick(item.id, it) },
                                onActionClick = { onLongClick(item.id, true) },
                                modifier = Modifier.semantics {
                                    testTag = chatItemTestTag
                                    contentDescription =
                                        Html.fromHtml(item.message).toString().trim()
                                }
                            )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScrollDownOnNewMessage(
    key: Any,
    listState: LazyListState,
) {
    LaunchedEffect(key) {
        if (listState.firstVisibleItemIndex <= 1) {
            listState.animateScrollToItem(0)
        }
    }
}

@Composable
fun DateChatItem(
    date: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceDim
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = date,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
private fun ChatScreenPreview() {
    ChatTheme(
        darkTheme = true
    ) {
        ChatLayout(
            ChatUiState(chatItems = chatItems),
            onArrowBackClick = {},
            onTextChange = {},
            onClick = {},
            onLongClick = { _, _ -> },
            onEmojiClick = {},
            onReactionClick = { _, _ -> },
            onDismissRequest = {},
            effect = null,
            onLastVisibleItemChange = {},
            onAddClick = {},
            onCopyButtonClick = {},
            onDeleteButtonClick = {},
            onEditButtonClick = {},
            onTopicSelected = {},
            onChangeTopicClick = {},
        )
    }
}

private val chatItems = listOf(
    ChatItem.Date("20 Марта"),
    ChatItem.Message(
        id = "1",
        icon = "https://i.pinimg.com/736x/bb/33/2f/bb332fa027610ceb7e5cbb8b39987c55.jpg",
        name = "Dasha",
        message = "A bun is rolling. Rolling… Rolling… And then he stops and says, \"I have a headache.\"",
        reactions = mutableListOf(),
    ),
    ChatItem.Date("21 Марта"),
    ChatItem.Message(
        id = "2",
        icon = "https://i.pinimg.com/736x/bb/33/2f/bb332fa027610ceb7e5cbb8b39987c55.jpg",
        name = "Dasha",
        message = "Hello world!",
        reactions = mutableListOf(),
    ),
    ChatItem.OwnMessage(
        id = "3",
        message = "Shut up",
        reactions = mutableListOf(),
    ),
)
