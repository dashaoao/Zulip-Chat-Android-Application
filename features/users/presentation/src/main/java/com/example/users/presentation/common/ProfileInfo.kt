package com.example.users.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.common.ui.ShimmerText
import com.example.common.ui.shimmerEffect
import com.example.common.ui.theme.ChatTheme
import com.example.users.domain.Presence

@Composable
fun ProfileInfo(
    username: String,
    icon: String?,
    mail: String,
    status: Presence,
) {
    ProfileInfoLayout(
        username,
        icon,
        mail,
        status,
    )
}

@Composable
fun ProfileInfoLayout(
    username: String,
    icon: String?,
    mail: String,
    status: Presence,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(vertical = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = icon ?: "https://i.ytimg.com/vi/Mmpi7hq_svk/hqdefault.jpg"
            ),
            contentDescription = "User Icon",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = username,
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = when (status) {
                Presence.ACTIVE -> "active"
                Presence.IDLE -> "idle"
                else -> "offline"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = when (status) {
                Presence.ACTIVE -> MaterialTheme.colorScheme.secondary
                Presence.IDLE -> MaterialTheme.colorScheme.tertiary
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            },
            fontSize = 16.sp,
        )
    }
}


@Composable
fun ProfileInfoShimmer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(vertical = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(8.dp))
        ShimmerText(
            textStyle = TextStyle(fontSize = 32.sp),
            modifier = Modifier.width(250.dp),
            shape = CircleShape
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.Bottom,
        ) {
            ShimmerText(
                textStyle = TextStyle(fontSize = 20.sp),
                modifier = Modifier.width(250.dp),
            )
            Spacer(modifier = Modifier.width(16.dp))
            ShimmerText(
                textStyle = TextStyle(fontSize = 16.sp),
                modifier = Modifier.width(50.dp),
            )
        }
    }
}


@Preview
@Composable
fun ProfileInfoPreview() {
    ChatTheme(
        darkTheme = true
    ) {
        ProfileInfoLayout(
            username = "Dasha",
            icon = "",
            mail = "aaa",
            status = Presence.OFFLINE,
        )
    }
}
