package com.example.channels.presentation.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.channels.presentation.model.StreamsType

@Composable
fun CustomTabRow(
    selectedType: StreamsType,
    onTabClick: (isSubscribed: Boolean) -> Unit,
) {
    val titles = StreamsType.entries.toList()
    var selectedTabIndex by remember { mutableIntStateOf(
        when (selectedType) {
            StreamsType.SUBSCRIBED -> {
                0
            }
            StreamsType.ALL -> {
                1
            }
        }
    ) }
    val indicatorColor = MaterialTheme.colorScheme.secondary

    Box(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            divider = { HorizontalDivider(color = Color.Transparent) },
            modifier = Modifier
                .padding(bottom = 10.dp),
            indicator = { tabPositions ->
                val currentTabPosition = tabPositions[selectedTabIndex]
                Canvas(
                    modifier = Modifier
                        .height(2.dp)
                        .fillMaxWidth()
                ) {
                    drawRect(
                        color = indicatorColor,
                        topLeft = Offset(
                            x = currentTabPosition.left.toPx() + currentTabPosition.width.toPx() * 0.17f,
                            y = size.height - 2.dp.toPx()
                        ),
                        size = Size(
                            width = currentTabPosition.width.toPx() * 0.7f,
                            height = size.height * 0.05f
                        )
                    )
                }
            },
        ) {
            titles.forEachIndexed { index, type ->
                Tab(
                    modifier = Modifier.padding(top = 8.dp),
                    text = {
                        Text(
                            text = stringResource(id = type.textResId),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Light
                        )
                    },
                    selectedContentColor = MaterialTheme.colorScheme.onSurface,
                    selected = type == selectedType,
                    onClick = {
                        selectedTabIndex = index
                        onTabClick(type == StreamsType.SUBSCRIBED)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun CustomTabRowPreview() {
    com.example.common.ui.theme.ChatTheme(
        darkTheme = true
    ) {
        CustomTabRow(
            onTabClick = {},
            selectedType = StreamsType.SUBSCRIBED,
        )
    }
}
