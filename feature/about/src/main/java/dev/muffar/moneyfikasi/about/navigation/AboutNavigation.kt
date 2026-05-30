package dev.muffar.moneyfikasi.about.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.about.AboutScreen
import dev.muffar.moneyfikasi.navigation.Screen

fun NavController.navigateToAbout() {
    navigate(Screen.About.route)
}

fun NavGraphBuilder.aboutNavGraph(
    onBackClick: () -> Unit,
) {
    composable(route = Screen.About.route) {
        AboutScreen(
            onBackClick = onBackClick
        )
    }
}

