package com.example.users.presentation.own_profile.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.users.presentation.own_profile.model.OwnProfileEffect
import com.example.users.presentation.own_profile.model.OwnProfileUIState
import kotlinx.coroutines.launch
import vivid.money.elmslie.compose.EffectWithKey

@Composable
fun OwnProfileLayout(
    state: OwnProfileUIState,
    effect: EffectWithKey<OwnProfileEffect>?,
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            CustomSnackbarHost(snackbarHostState)
        },
    ) { padding ->
        effect?.takeIfInstanceOf<OwnProfileEffect.Error>()?.key?.let {
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
fun OwnProfilePreview() {
    ChatTheme(
        darkTheme = true
    ) {
        OwnProfileLayout(
            OwnProfileUIState(
                username = "Dasha",
                icon = "",
                mail = "aaa",
                status = Presence.OFFLINE,
            ),
            effect = null
        )
    }
}
