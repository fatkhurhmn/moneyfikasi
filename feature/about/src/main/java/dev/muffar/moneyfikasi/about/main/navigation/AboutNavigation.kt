package dev.muffar.moneyfikasi.about.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.about.licenses.OpenSourceLicensesScreen
import dev.muffar.moneyfikasi.about.main.AboutScreen
import dev.muffar.moneyfikasi.navigation.Screen

fun NavController.navigateToAbout() {
    navigate(Screen.About.route)
}

fun NavController.navigateToOpenSourceLicenses() {
    navigate(Screen.OpenSourceLicenses.route)
}

fun NavGraphBuilder.aboutNavGraph(
    onBackClick: () -> Unit,
    onOpenSourceLicensesClick: () -> Unit,
) {
    composable(route = Screen.About.route) {
        AboutScreen(
            onBackClick = onBackClick,
            onOpenSourceLicensesClick = onOpenSourceLicensesClick
        )
    }

    composable(route = Screen.OpenSourceLicenses.route) {
        OpenSourceLicensesScreen(
            onBackClick = onBackClick
        )
    }
}
