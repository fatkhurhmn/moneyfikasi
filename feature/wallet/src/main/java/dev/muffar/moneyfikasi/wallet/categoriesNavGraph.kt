package dev.muffar.moneyfikasi.wallet

import androidx.navigation.NavGraphBuilder
import dev.muffar.moneyfikasi.wallet.add_edit.navigation.addEditWalletNavigation
import dev.muffar.moneyfikasi.wallet.list.navigation.walletsNavigation
import java.util.UUID

fun NavGraphBuilder.walletsNavGraph(
    navigateToAddWallet : () -> Unit,
    navigateToEditWallet : (UUID) -> Unit,
    navigateBack: () -> Unit,
) {
    walletsNavigation(
        navigateToAddWallet = navigateToAddWallet,
        navigateToEditWallet = navigateToEditWallet,
        navigateBack = navigateBack,
    )

    addEditWalletNavigation (
        navigateBack = navigateBack,
    )
}