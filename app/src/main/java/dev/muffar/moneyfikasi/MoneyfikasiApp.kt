package dev.muffar.moneyfikasi

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
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
        Screen.Transaction.route,
        Screen.Statistics.route,
        Screen.Debt.route,
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
        floatingActionButton = { AddActionButton() },
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

@Composable
fun AddActionButton(
    onClick: () -> Unit = {},
) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = "Add",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}