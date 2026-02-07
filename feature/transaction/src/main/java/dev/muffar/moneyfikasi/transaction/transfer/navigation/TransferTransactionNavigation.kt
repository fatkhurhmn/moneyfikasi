package dev.muffar.moneyfikasi.transaction.transfer.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.transaction.transfer.TransferTransactionEvent
import dev.muffar.moneyfikasi.transaction.transfer.TransferTransactionScreen
import dev.muffar.moneyfikasi.transaction.transfer.TransferTransactionViewModel
import java.util.UUID

fun NavGraphBuilder.transferTransactionNavigation(
    onNavigateBack: () -> Unit,
    onNavigateToAddWallet: () -> Unit,
) {
    composable(Screen.TransferTransaction.route) {
        val viewModel = hiltViewModel<TransferTransactionViewModel>()
        val state by viewModel.state.collectAsState()
        val event = viewModel::onEvent
        val eventFlow = viewModel.eventFlow

        TransferTransactionScreen(
            modifier = Modifier,
            state = state,
            eventFlow = eventFlow,
            onAmountChange = { amount ->
                event(TransferTransactionEvent.AmountChanged(amount))
            },
            onFeeChange = { fee ->
                event(TransferTransactionEvent.FeeChanged(fee))
            },
            onSourceWalletSelect = { wallet ->
                event(TransferTransactionEvent.SourceWalletSelected(wallet))
            },
            onTargetWalletSelect = { wallet ->
                event(TransferTransactionEvent.TargetWalletSelected(wallet))
            },
            onDateSelect = { date ->
                event(TransferTransactionEvent.DateSelected(date))
            },
            onTimeSelect = { hour, minute ->
                event(TransferTransactionEvent.TimeSelected(hour, minute))
            },
            onBackClick = onNavigateBack,
            onCreateClick = {
                event(TransferTransactionEvent.SaveTransfer)
            },
            onShowBottomSheet = { sheetType ->
                event(TransferTransactionEvent.OnBottomSheetChange(sheetType))
            },
            onAddWallet = onNavigateToAddWallet,
        )
    }
}

fun NavController.toTransferTransactionScreen(id: UUID? = null) {
    navigate(Screen.TransferTransaction.routeWithArg(id))
}