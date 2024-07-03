package com.example.common.ui

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition(label = "")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1500)
        ), label = ""
    )
    val color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp).copy(alpha = 0.5f)
    val lineColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                color,
                lineColor,
                color,
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}

@Composable
fun ShimmerText(
    textStyle: TextStyle,
    modifier: Modifier,
    shape: CornerBasedShape = MaterialTheme.shapes.medium,
) {
    Text(
        text = "",
        style = textStyle,
        modifier = modifier
            .clip(shape)
            .shimmerEffect(),
    )
}

@Composable
fun ShimmerCheckBox(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier
            .padding(14.dp)
            .size(20.dp)
            .clip(CircleShape)
            .shimmerEffect()
    )
}

