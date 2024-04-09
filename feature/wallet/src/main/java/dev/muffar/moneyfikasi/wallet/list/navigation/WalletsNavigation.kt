package dev.muffar.moneyfikasi.wallet.list.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.wallet.list.WalletsScreen
import dev.muffar.moneyfikasi.wallet.list.WalletsViewModel
import java.util.UUID

fun NavGraphBuilder.walletsNavigation(
    navigateToAddWallet: () -> Unit,
    navigateToEditWallet: (UUID) -> Unit,
    navigateBack: () -> Unit,
) {
    composable(route = Screen.Wallets.route) {
        val viewModel = hiltViewModel<WalletsViewModel>()
        val state = viewModel.state.value

        WalletsScreen(
            state = state,
            onAddWalletClick = navigateToAddWallet,
            onWalletItemClick = navigateToEditWallet,
            onBackClick = navigateBack
        )
    }
}

fun NavController.toWalletsScreen() {
    navigate(Screen.Wallets.route)
}