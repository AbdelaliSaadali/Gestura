package com.gestura.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.School
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector,
) {
    data object Home : Screen(
        route = "home",
        title = "Home",
        icon = Icons.Filled.Home,
    )

    data object VoiceToSign : Screen(
        route = "voice_to_sign",
        title = "Voice",
        icon = Icons.Filled.RecordVoiceOver,
    )

    data object SignToText : Screen(
        route = "sign_to_text",
        title = "Camera",
        icon = Icons.Filled.Videocam,
    )

    data object Learning : Screen(
        route = "learning",
        title = "Learn",
        icon = Icons.Filled.School,
    )
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.VoiceToSign,
    Screen.SignToText,
    Screen.Learning,
)

