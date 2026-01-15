package dev.muffar.moneyfikasi.transaction.transfer.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
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
                event(TransferTransactionEvent.OnAmountChange(amount))
            },
            onFeeChange = { fee ->
                event(TransferTransactionEvent.OnFeeChange(fee))
            },
            onSourceWalletSelect = { wallet ->
                event(TransferTransactionEvent.OnSourceWalletSelect(wallet))
            },
            onTargetWalletSelect = { wallet ->
                event(TransferTransactionEvent.OnTargetWalletSelect(wallet))
            },
            onDateSelect = { date ->
                event(TransferTransactionEvent.OnDateSelect(date))
            },
            onTimeSelect = { hour, minute ->
                event(TransferTransactionEvent.OnTimeSelect(hour, minute))
            },
            onBackClick = onNavigateBack,
            onCreateClick = {
                event(TransferTransactionEvent.OnCreateClicked)
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