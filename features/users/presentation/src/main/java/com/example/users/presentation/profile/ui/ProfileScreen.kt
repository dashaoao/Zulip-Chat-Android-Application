package com.example.users.presentation.profile.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.common.ui.CustomSnackbarHost
import com.example.common.ui.theme.ChatTheme
import com.example.users.domain.Presence
import com.example.users.presentation.common.ProfileInfo
import com.example.users.presentation.common.ProfileInfoShimmer
import com.example.users.presentation.profile.model.ProfileEffect
import com.example.users.presentation.profile.model.ProfileUIState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import vivid.money.elmslie.compose.EffectWithKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileLayout(
    state: ProfileUIState,
    effect: EffectWithKey<ProfileEffect>?,
    onArrowBackClick: () -> Unit,
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
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onArrowBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = containerColor,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface,
                )
            )
        }
    ) { padding ->
        effect?.takeIfInstanceOf<ProfileEffect.Error>()?.key?.let {
            LaunchedEffect(it) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = it.value.msg.stringValue(context),
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }

        Box(
            Modifier.padding(padding)
        ) {
            if (state.isLoading) {
                ProfileInfoShimmer()
            } else {
                ProfileInfo(
                    username = state.username,
                    icon = state.icon,
                    mail = state.mail,
                    status = state.status
                )
            }
        }
    }
}

@Preview
@Composable
fun ProfilePreview() {
    ChatTheme(
        darkTheme = false
    ) {
        ProfileLayout(
            ProfileUIState(
                username = "Dasha",
                icon = "",
                mail = "aaa",
                status = Presence.OFFLINE,
            ),
            onArrowBackClick = {},
            effect = null,
        )
    }
}
