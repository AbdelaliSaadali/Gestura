package com.gestura.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gestura.app.screens.HomeScreen
import com.gestura.app.screens.LearningLessonScreen
import com.gestura.app.screens.SignToTextScreen
import com.gestura.app.screens.VoiceToSignScreen

@Composable
fun GesturaNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
    ) {
        composable(Screen.Home.route) {
            HomeScreen()
        }
        composable(Screen.VoiceToSign.route) {
            VoiceToSignScreen()
        }
        composable(Screen.SignToText.route) {
            SignToTextScreen()
        }
        composable(Screen.Learning.route) {
            LearningLessonScreen()
        }
    }
}

