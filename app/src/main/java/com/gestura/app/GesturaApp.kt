package com.gestura.app

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gestura.app.navigation.GesturaNavGraph
import com.gestura.app.navigation.Screen
import com.gestura.app.navigation.bottomNavItems
import com.gestura.app.ui.theme.*

@Composable
fun GesturaApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Hide bottom bar on immersive screens if desired
    val showBottomBar = currentDestination?.route in bottomNavItems.map { it.route }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundLight,
        bottomBar = {
            if (showBottomBar) {
                GesturaBottomBar(
                    currentRoute = currentDestination?.route,
                    onNavigate = { screen ->
                        navController.navigate(screen.route) {
                            // Pop up to the start destination to avoid back-stack buildup
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            GesturaNavGraph(navController = navController)
        }
    }
}

@Composable
private fun GesturaBottomBar(
    currentRoute: String?,
    onNavigate: (Screen) -> Unit,
) {
    NavigationBar(
        containerColor = SurfaceLight,
        contentColor = TextMuted,
        tonalElevation = 0.dp,
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .height(72.dp),
    ) {
        bottomNavItems.forEach { screen ->
            val isSelected = currentRoute == screen.route

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(screen) },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title,
                        modifier = Modifier.size(24.dp),
                    )
                },
                label = {
                    Text(
                        text = screen.title,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Primary,
                    selectedTextColor = Primary,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted,
                    indicatorColor = PrimaryContainer,
                ),
            )
        }
    }
}

