package dev.muffar.moneyfikasi

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.muffar.moneyfikasi.common_ui.component.ExpandableFloatingActionButton
import dev.muffar.moneyfikasi.navigation.MainBottomNav
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.transaction.add_edit.navigation.toAddEditTransactionScreen

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
    var isExpanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = {
                    if (isExpanded) {
                        isExpanded = false
                    }
                }
            )
        },
        bottomBar = {
            if (isBottomNavVisible) {
                MainBottomNav(navController = navController)
            }
        },
        floatingActionButton = {
            if (isAddButtonVisible) {
                ExpandableFloatingActionButton(
                    isExpanded = isExpanded,
                    onClick = { isExpanded = !isExpanded },
                    fabIcon = Icons.Rounded.Add,
                    onTransactionClick = {
                        navController.toAddEditTransactionScreen(it)
                        isExpanded = false
                    }
                )
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