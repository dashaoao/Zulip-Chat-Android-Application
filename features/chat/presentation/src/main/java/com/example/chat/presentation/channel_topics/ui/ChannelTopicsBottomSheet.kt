package com.example.chat.presentation.channel_topics.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chat.presentation.R
import com.example.chat.presentation.channel_topics.ChannelTopicsViewModel
import com.example.chat.presentation.channel_topics.di.ChannelTopicsComponentViewModel
import com.example.common.ui.TopicId
import com.example.common.ui.daggerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelTopicsBottomSheet(
    topicId: TopicId,
    onTopicSelected: (topicId: TopicId) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val componentViewModel = viewModel<ChannelTopicsComponentViewModel>()
    val viewModel = daggerViewModel {
        componentViewModel.channelTopicsComponent.getViewModel()
    }

    LaunchedEffect(topicId) {
        viewModel.getChannelTopics(topicId)
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        tonalElevation = 0.dp,
        shape = MaterialTheme.shapes.medium,
        containerColor = Color.Transparent,
        dragHandle = { },
    ) {
        ChannelTopicsBottomSheetLayout(
            onTopicSelected = onTopicSelected,
            viewModel = viewModel,
        )
    }
}

@Composable
fun ChannelTopicsBottomSheetLayout(
    onTopicSelected: (topicId: TopicId) -> Unit,
    viewModel: ChannelTopicsViewModel
) {
    val state by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.error.collect { errorMessage ->
            Toast.makeText(context, errorMessage.stringValue(context), Toast.LENGTH_SHORT).show()
        }
    }

    Surface(
        color = Color.LightGray,
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 40.dp)
        ) {
            if (state.topics.isEmpty()) {
                item {
                    Text(
                        text = stringResource(id = R.string.no_topics),
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 16.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Normal,
                    )
                }
            }

            items(items = state.topics, key = { item -> item.topicId }) { topic ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .drawBehind {
                            val strokeWidth = 0.5.dp.toPx()
                            val y = size.height - strokeWidth / 2
                            drawLine(
                                color = Color.Gray,
                                start = Offset(0f, y),
                                end = Offset(size.width, y),
                                strokeWidth = strokeWidth
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = topic.title,
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .clickable { onTopicSelected(topic.topicId) }
                            .padding(8.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Normal,
                    )
                }
            }
        }
    }
}
