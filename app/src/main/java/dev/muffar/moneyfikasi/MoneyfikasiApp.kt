package dev.muffar.moneyfikasi

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.muffar.moneyfikasi.navigation.MainBottomNav
import dev.muffar.moneyfikasi.navigation.Screen

@Composable
fun MoneyfikasiApp(
    navController: NavHostController,
) {
    val mainRoute = listOf(
        Screen.Transactions.route,
        Screen.Search.route,
        Screen.Statistic.route,
        Screen.Settings.route,
    )

    val navigationBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navigationBackStackEntry?.destination?.route
    val isBottomNavVisible = mainRoute.contains(currentRoute)

    Scaffold(
        bottomBar = {
            if (isBottomNavVisible) {
                MainBottomNav(navController = navController)
            }
        },
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            RootNavigation(
                navController = navController
            )
        }
    }
}