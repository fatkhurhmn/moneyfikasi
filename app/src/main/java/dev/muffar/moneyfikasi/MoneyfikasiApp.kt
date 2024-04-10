package dev.muffar.moneyfikasi

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.muffar.moneyfikasi.common_ui.component.CommonAddButton
import dev.muffar.moneyfikasi.navigation.MainBottomNav
import dev.muffar.moneyfikasi.navigation.Screen

@Composable
fun MoneyfikasiApp(
    navController: NavHostController,
) {
    val mainRoute = listOf(
        Screen.Transaction.route,
        Screen.Statistics.route,
        Screen.Debt.route,
        Screen.Settings.route,
    )

    val navigationBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navigationBackStackEntry?.destination?.route
    val isBottomNavVisible = mainRoute.contains(currentRoute)
    val isAddButtonVisible = currentRoute == Screen.Transaction.route

    Scaffold(
        bottomBar = {
            if (isBottomNavVisible) {
                MainBottomNav(navController = navController)
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = isAddButtonVisible,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                CommonAddButton {}
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