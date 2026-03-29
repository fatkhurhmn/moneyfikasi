package dev.muffar.moneyfikasi.quick_transaction

import androidx.navigation.NavGraphBuilder
import dev.muffar.moneyfikasi.quick_transaction.list.navigation.quickTransactionsNavigation

fun NavGraphBuilder.quickTransactionGraph(
    navigateBack: () -> Unit
) {
    quickTransactionsNavigation(
        navigateBack = navigateBack
    )
}