package dev.muffar.moneyfikasi.splash.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.splash.SplashScreen

fun NavGraphBuilder.splashNavGraph() {
    composable(Screen.Splash.route) {
        SplashScreen()
    }
}
