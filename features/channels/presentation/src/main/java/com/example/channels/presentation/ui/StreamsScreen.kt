package com.example.channels.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import com.example.channels.presentation.model.ChannelsItem
import com.example.common.ui.TopicId

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StreamsScreen(
    items: List<ChannelsItem>,
    onChannelClick: (channelId: String, currentData: List<ChannelsItem>) -> Unit,
    onTopicClick: (id: TopicId) -> Unit,
    isLoading: Boolean,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = topicsListContentDesc
                testTag = topicsListTestTag
            },
    ) {
        if (isLoading) {
            items(items = listOf(0, 0, 0, 0)) {
                ShimmerChannelRow()
            }
        } else {
            itemsIndexed(items = items, key = { _, item -> item.id }) { index, item ->
                when (item) {
                    is ChannelsItem.Channel -> {
                        ChannelRow(
                            channel = item,
                            onChannelClick,
                            item.isTopicLoading,
                            channels = items,
                            modifier = Modifier.semantics {
                                testTag = getTopicItemTestTagById(item)
                                listItemPosition = index
                            }.animateItemPlacement()
                        )
                    }

                    is ChannelsItem.Topic -> {
                        TopicRow(
                            topic = item,
                            onTopicClick,
                            index,
                            modifier = Modifier.semantics {
                                testTag = getTopicItemTestTagById(item)
                                listItemPosition = index
                            }.animateItemPlacement()
                        )
                    }
                }
            }
        }
    }
}
