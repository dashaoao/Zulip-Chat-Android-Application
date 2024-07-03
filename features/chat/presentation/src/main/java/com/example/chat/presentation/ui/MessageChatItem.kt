package com.example.chat.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.chat.presentation.ui.custom_message.CustomMessage
import com.example.chat.presentation.ui.custom_message.CustomOwnMessage
import com.example.chat.presentation.ui.custom_message.model.ReactionUi

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageChatItem(
    icon: String,
    name: String,
    message: String,
    reactions: List<ReactionUi>,
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit,
    onActionClick: () -> Unit,
    onReactionClick: (ReactionUi) -> Unit,
) {
    Box(modifier = modifier.combinedClickable(onLongClick = onAddClick, onClick = {} )){
        AndroidView(
            modifier = modifier.padding(end = 40.dp),
            factory = {
                CustomMessage(it).apply {
                    onAddReaction = onAddClick
                    onClick = onReactionClick
                    onLongClick = onActionClick
                }

            },
            update = {
                it.apply {
                    setAvatar(icon)
                    setName(name)
                    setMessage(message)
                    setReactions(reactions)
                }
            }
        )
    }
}

@Composable
fun OwnMessageChatItem(
    message: String,
    reactions: List<ReactionUi>,
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit,
    onActionClick: () -> Unit,
    onReactionClick: (ReactionUi) -> Unit,
) {
    AndroidView(
        modifier = modifier.padding(start = 40.dp),
        factory = {
            CustomOwnMessage(it).apply {
                onAddReaction = onAddClick
                onClick = onReactionClick
                onLongClick = onActionClick
            }
        },
        update = {
            it.apply {
                setMessage(message)
                setReactions(reactions)
            }
        }
    )
}
