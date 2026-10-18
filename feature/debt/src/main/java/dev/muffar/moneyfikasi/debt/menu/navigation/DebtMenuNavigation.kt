package dev.muffar.moneyfikasi.debt.menu.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.debt.menu.DebtMenuScreen
import dev.muffar.moneyfikasi.debt.menu.DebtMenuViewModel
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.navigation.Screen

fun NavGraphBuilder.debtMenuNavigation(
    navigateToDebtList: (DebtType) -> Unit,
    navigateToAddDebt: (DebtType) -> Unit,
    navigateBack: () -> Unit,
) {
    composable(route = Screen.DebtMenu.route) {
        val viewModel = hiltViewModel<DebtMenuViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        DebtMenuScreen(
            state = state,
            onDebtClick = navigateToDebtList,
            onAddDebtClick = navigateToAddDebt,
            onBackClick = navigateBack
        )
    }
}

fun NavController.toDebtMenuScreen() {
    this.navigate(Screen.DebtMenu.route)
}
