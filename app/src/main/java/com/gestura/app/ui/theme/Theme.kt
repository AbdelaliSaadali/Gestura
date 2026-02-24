package com.gestura.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = TextOnDark,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = TextMuted,
    onSecondary = TextOnDark,
    tertiary = Accent,
    onTertiary = TextMain,
    background = BackgroundLight,
    onBackground = TextMain,
    surface = SurfaceLight,
    onSurface = TextMain,
    surfaceVariant = BackgroundLight,
    onSurfaceVariant = TextMuted,
    outline = DividerLight,
    error = Error,
    onError = TextOnDark,
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryContainer,
    primaryContainer = PrimaryDark,
    onPrimaryContainer = PrimaryContainer,
    secondary = TextOnDarkMuted,
    onSecondary = TextMain,
    tertiary = Accent,
    onTertiary = TextMain,
    background = BackgroundDark,
    onBackground = TextOnDark,
    surface = SurfaceDark,
    onSurface = TextOnDark,
    surfaceVariant = SurfaceDark,
    onSurfaceVariant = TextOnDarkMuted,
    outline = SurfaceDark,
    error = Error,
    onError = TextOnDark,
)

@Composable
fun GesturaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = GesturaTypography,
        content = content,
    )
}

