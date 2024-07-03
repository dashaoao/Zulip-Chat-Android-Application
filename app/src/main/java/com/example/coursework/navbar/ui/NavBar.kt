package com.example.coursework.navbar.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.ui.theme.ChatTheme
import com.example.coursework.navbar.model.NavBarRoute

@Composable
fun NavBar(
    screens: List<NavBarRoute>,
    selected: NavBarRoute,
    onClick: (NavBarRoute) -> Unit,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
    ) {
        screens.forEach { screen ->
            NavBarItem(
                screen = screen,
                selected = screen == selected,
                onClick = {
                    onClick(screen)
                }
            )
        }
    }
}

@Composable
fun RowScope.NavBarItem(
    screen: NavBarRoute,
    selected: Boolean,
    onClick: () -> Unit,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                painter = painterResource(id = screen.iconResId),
                modifier = Modifier
                    .size(24.dp)
                    .indication(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ),
                contentDescription = null
            )
        },
        label = {
            Text(
                text = stringResource(id = screen.titleResId),
                fontSize = 12.sp,
            )
        },
        colors = NavigationBarItemDefaults.colors(
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
            indicatorColor = MaterialTheme.colorScheme.surface,
        )
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
private fun NavBarPreview() {
    val screens = remember {
        listOf(NavBarRoute.Channels, NavBarRoute.People, NavBarRoute.Profile)
    }

    com.example.common.ui.theme.ChatTheme(darkTheme = true) {
        NavBar(screens = screens, onClick = {}, selected = NavBarRoute.Channels)
    }
}
