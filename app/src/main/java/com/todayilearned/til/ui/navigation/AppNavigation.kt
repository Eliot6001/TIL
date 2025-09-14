package com.todayilearned.til.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.todayilearned.til.ui.ViewModel.TilViewModel

import com.todayilearned.til.ui.screens.welcome.WelcomeScreen
import com.todayilearned.til.ui.screens.MainScreen
import kotlinx.coroutines.Job


@Composable
fun AppNavigation(
    tilViewModel: TilViewModel,
    preload: Boolean,
    isDark: Boolean,
    onThemeToggle: () -> Job
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") {
            WelcomeScreen(
                onGoogleLoginClick = { navController.navigate("main") },
                onSkipClick = { navController.navigate("main") }
            )
        }
        composable("main") {
            MainScreen(tilViewModel,isDark,onThemeToggle = onThemeToggle)
        }
    }
}