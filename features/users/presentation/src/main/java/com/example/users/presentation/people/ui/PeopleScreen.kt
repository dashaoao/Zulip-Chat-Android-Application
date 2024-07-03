package com.example.users.presentation.people.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.common.ui.CustomSnackbarHost
import com.example.common.ui.ShimmerText
import com.example.common.ui.shimmerEffect
import com.example.common.ui.theme.ChatTheme
import com.example.users.domain.Presence
import com.example.users.presentation.people.model.PeopleEffect
import com.example.users.presentation.people.model.PeopleUIState
import com.example.users.presentation.people.model.UserItem
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import vivid.money.elmslie.compose.EffectWithKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeopleLayout(
    state: PeopleUIState,
    effect: EffectWithKey<PeopleEffect>?,
    onUserClick: (idUser: String) -> Unit,
    performSearch: (text: String) -> Unit,
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

    Scaffold(
        snackbarHost = {
            CustomSnackbarHost(snackbarHostState)
        },
        topBar = {
            var textValue by remember { mutableStateOf(TextFieldValue()) }
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        TextField(
                            value = textValue.text,
                            onValueChange = { newValue ->
                                textValue = TextFieldValue(newValue)
                                performSearch(newValue)
                            },
                            placeholder = {
                                Text(
                                    text = "Users...",
                                    fontSize = 24.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Light
                                )
                            },
                            singleLine = true,
                            colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent),
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterStart)
                                .padding(end = 48.dp, bottom = 8.dp)
                        )
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = null,
                            modifier = Modifier
                                .size(38.dp)
                                .align(Alignment.CenterEnd),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
            )
        },
    ) { padding ->
        effect?.takeIfInstanceOf<PeopleEffect.Error>()?.key?.let {
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

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {

            if (state.isLoading) {
                items(items = listOf(0, 0)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(65.dp)
                                .clip(CircleShape)
                                .shimmerEffect()
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            ShimmerText(
                                textStyle = TextStyle(fontSize = 22.sp),
                                modifier = Modifier.width(150.dp),
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            ShimmerText(
                                textStyle = TextStyle(fontSize = 16.sp),
                                modifier = Modifier.width(200.dp),
                            )
                        }
                    }
                }
            } else {
                items(items = state.users, key = { item -> item.id }) { user ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onUserClick(user.id) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(65.dp)) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = user.icon
                                        ?: "https://i.ytimg.com/vi/Mmpi7hq_svk/hqdefault.jpg"
                                ),
                                contentDescription = "User Icon",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                            )
                            if (user.status == Presence.ACTIVE) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .size(12.dp)
                                        .offset(x = (-4).dp, y = (-4).dp)
                                        .background(
                                            MaterialTheme.colorScheme.secondary,
                                            CircleShape
                                        )
                                )
                            } else if (user.status == Presence.IDLE) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .size(12.dp)
                                        .offset(x = (-4).dp, y = (-4).dp)
                                        .background(
                                            MaterialTheme.colorScheme.tertiary,
                                            CircleShape
                                        )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = user.username,
                                fontSize = 22.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                text = user.mail,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.surfaceTint
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PeoplePreview() {
    ChatTheme(
        darkTheme = true
    ) {
        PeopleLayout(
            PeopleUIState(users = users),
            onUserClick = {},
            effect = null,
            performSearch = {}
        )
    }
}

private val users: List<UserItem> = listOf(
    UserItem(
        id = "1",
        username = "Dima",
        icon = "https://android-obzor.com/wp-content/uploads/2022/03/28e4ac42f547e6ac0f50f7cfa916ca93.jpg",
        mail = "anonimus@yandex.ru",
        status = Presence.IDLE,
    )
)
