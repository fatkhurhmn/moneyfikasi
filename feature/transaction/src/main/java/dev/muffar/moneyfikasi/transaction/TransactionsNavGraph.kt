package dev.muffar.moneyfikasi.transaction

import androidx.navigation.NavGraphBuilder
import dev.muffar.moneyfikasi.transaction.add_edit.navigation.addEditTransactionNavigation
import dev.muffar.moneyfikasi.transaction.detail.navigation.transactionDetailNavigation
import dev.muffar.moneyfikasi.transaction.list.navigation.transactionsNavigation
import java.util.UUID

fun NavGraphBuilder.transactionsNavGraph(
    onNavigateBack: () -> Unit,
    onNavigateToTransactionDetail: (UUID) -> Unit,
) {

    transactionsNavigation(
        onNavigateToTransactionDetail = onNavigateToTransactionDetail
    )

    addEditTransactionNavigation(
        onNavigateBack = onNavigateBack
    )

    transactionDetailNavigation(
        onNavigateBack = onNavigateBack,
    )
}