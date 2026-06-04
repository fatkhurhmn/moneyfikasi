package dev.muffar.moneyfikasi

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.muffar.moneyfikasi.navigation.MainBottomNav
import dev.muffar.moneyfikasi.navigation.MainNavigation
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.transaction.add_edit.navigation.toAddEditTransactionScreen
import dev.muffar.moneyfikasi.transaction.transfer.navigation.toTransferTransactionScreen

@Composable
fun MainScreen(
    navController: NavHostController,
    startDestination: String,
) {
    val mainRoute = listOf(
        Screen.Home.route,
        Screen.Transactions.route,
        Screen.Statistic.route,
        Screen.More.route,
    )

    val navigationBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navigationBackStackEntry?.destination?.route
    val isBottomNavVisible = mainRoute.contains(currentRoute)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            if (isBottomNavVisible) {
                MainBottomNav(
                    navController = navController,
                    onAddTransaction = { type ->
                        if (type != null) {
                            navController.toAddEditTransactionScreen(type)
                        } else {
                            navController.toTransferTransactionScreen()
                        }
                    }
                )
            }
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) {
        Box(
            modifier = Modifier
                .consumeWindowInsets(it)
        ) {
            MainNavigation(
                navController = navController,
                startDestination = startDestination
            )
        }
    }
}
