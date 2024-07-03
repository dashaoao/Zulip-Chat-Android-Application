package com.example.common.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Teal,
    onPrimary = Color.White,
    secondary = Green,
    onSecondary = Color.White,
    tertiary = Yellow,
    surface = NeutralDark,
    surfaceVariant = Neutral,
    surfaceDim = NeutralAlmostBlack,
    onSurface = Color.White,
    background = NeutralDarker,
    onSurfaceVariant = NeutralLight,
    surfaceContainer = NeutralLight,
    surfaceTint = NeutralLighter,
)

private val LightColorScheme = lightColorScheme(
    primary = Teal,
    onPrimary = Color.White,
    secondary = Green,
    onSecondary = Color.Black,
    tertiary = YellowLight,
    onTertiary = Color.Black,
    surfaceVariant = Color.White,
    surface = Color(0xFFE0E0E0),
    surfaceDim = Color(0xFFEEEEEE),
    surfaceTint = NeutralLighter,
    surfaceContainer = Color(0xFFCCCCCC),
    onSurface = Color.Black,
    onSurfaceVariant = NeutralDark,
    background = Color.White,
)
@Composable
fun ChatTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
