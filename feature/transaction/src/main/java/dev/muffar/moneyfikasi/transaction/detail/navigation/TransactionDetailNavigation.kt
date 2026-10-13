package dev.muffar.moneyfikasi.transaction.detail.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.transaction.detail.TransactionDetailEvent
import dev.muffar.moneyfikasi.transaction.detail.TransactionDetailScreen
import dev.muffar.moneyfikasi.transaction.detail.TransactionDetailViewModel
import java.util.UUID

fun NavGraphBuilder.transactionDetailNavigation(
    onNavigateToEditTransaction: (TransactionType?, UUID) -> Unit,
    onNavigateToEditDebt: (DebtType, UUID) -> Unit,
    onNavigateToTransactionDetail: (UUID) -> Unit,
    onNavigateBack: () -> Unit,
) {
    composable(
        route = Screen.TransactionDetail.route,
        arguments = listOf(
            navArgument(
                name = Screen.TransactionDetail.IS_TRANSFER
            ) {
                type = NavType.BoolType
            }
        ),
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "moneyfikasi://transaction_detail/{transaction_id}/{is_transfer}"
            }
        )
    ) {
        val viewModel = hiltViewModel<TransactionDetailViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val event = viewModel::onEvent
        val eventFlow = viewModel.eventFlow

        LaunchedEffect(Unit) {
            event(TransactionDetailEvent.InitData)
        }

        TransactionDetailScreen(
            state = state,
            eventFlow = eventFlow,
            onDelete = { event(TransactionDetailEvent.DeleteTransaction) },
            onShowAlert = { event(TransactionDetailEvent.ShowDeleteAlert(it)) },
            onEditClick = onNavigateToEditTransaction,
            onDebtEditClick = onNavigateToEditDebt,
            onBackClick = onNavigateBack,
            onSaveClick = { event(TransactionDetailEvent.SaveToGallery(it)) },
            onPlusClick = { event(TransactionDetailEvent.ShowPlusSheet(true)) },
            onPlusTabSelected = { event(TransactionDetailEvent.PlusTabSelected(it)) },
            onPlusAmountChange = { event(TransactionDetailEvent.PlusAmountChanged(it)) },
            onPlusConfirm = { event(TransactionDetailEvent.PlusConfirmed) },
            onPlusDismiss = { event(TransactionDetailEvent.PlusDismissed) },
            onHistoryClick = onNavigateToTransactionDetail,
        )
    }
}

fun NavController.toTransactionDetail(id: UUID, isTransfer: Boolean) {
    navigate(Screen.TransactionDetail.routeWithArg(id, isTransfer))
}