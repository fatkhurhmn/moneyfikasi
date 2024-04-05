package dev.muffar.moneyfikasi.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.settings.SettingsScreen

fun NavGraphBuilder.settingsNavGraph() {
    composable(route = Screen.Settings.route) {
        SettingsScreen()
    }
}