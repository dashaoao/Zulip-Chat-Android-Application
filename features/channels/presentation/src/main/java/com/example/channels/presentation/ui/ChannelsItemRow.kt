package com.example.channels.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.channels.presentation.model.ChannelsItem
import com.example.common.ui.TopicId

@Composable
fun ChannelRow(
    channel: ChannelsItem.Channel,
    onChannelClick: (channelId: String, currentData: List<ChannelsItem>) -> Unit,
    isTopicLoading: Boolean,
    channels: List<ChannelsItem>,
    modifier: Modifier = Modifier,
) {
    val alpha = if (channel.expanded) {
        1f
    } else {
        0.5f
    }
    Row(
        modifier = modifier
            .fillMaxSize()
            .clickable(onClick = { onChannelClick(channel.id, channels) })
            .drawBehind {
                val strokeWidth = 0.5.dp.toPx()
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = Color.Gray,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = channel.title,
            fontSize = 22.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Box(modifier = Modifier.size(50.dp)) {
            if (isTopicLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(start = 8.dp)
                        .size(20.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    strokeWidth = 4.dp,
                )
            } else {
                IconButton(
                    onClick = { onChannelClick(channel.id, channels) },
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Icon(
                        imageVector = if (channel.expanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier
                            .scale(1.6f)
                            .alpha(alpha)
                    )
                }
            }
        }

    }
}

@Composable
fun ShimmerChannelRow() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        com.example.common.ui.ShimmerText(
            textStyle = TextStyle(fontSize = 22.sp),
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = {},
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier
                    .scale(1.6f)
                    .alpha(0.5f)
            )
        }
    }
}

@Composable
fun TopicRow(
    topic: ChannelsItem.Topic,
    onTopicClick: (id: TopicId) -> Unit,
    numb: Int,
    modifier: Modifier = Modifier,
) {
    val bgColor = if (numb % 2 == 0) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.tertiary
    }
    Box(
        modifier = modifier
            .background(bgColor)
            .clickable { onTopicClick(topic.topicId) },
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = topic.title,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.semantics { testTag = topicNameTestTag }
            )

            topic.messageCount?.let {
                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "$it mes",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(1.dp))
}
