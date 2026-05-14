package dev.muffar.moneyfikasi.transaction.transfer

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarMessage
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.transaction.transfer.component.TransferTransactionButton
import dev.muffar.moneyfikasi.transaction.transfer.component.TransferTransactionForm
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferTransactionScreen(
    modifier: Modifier = Modifier,
    state: TransferTransactionState,
    eventFlow: SharedFlow<TransferTransactionViewModel.UiEvent>,
    onAmountChange: (String) -> Unit,
    onFeeChange: (String) -> Unit,
    onSourceWalletSelect: (Wallet) -> Unit,
    onTargetWalletSelect: (Wallet) -> Unit,
    onDateSelect: (Long) -> Unit,
    onTimeSelect: (Pair<Int, Int>) -> Unit,
    onBackClick: () -> Unit,
    onAddWallet: () -> Unit,
    onTransfer: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    Scaffold(
        snackbarHost = {
            SnackbarMessage(state = snackbarHostState)
        },
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.transfer),
                onBackClick = onBackClick
            )
        },
        bottomBar = { TransferTransactionButton(onTransfer) }
    ) {
        TransferTransactionForm(
            modifier = modifier
                .padding(it)
                .consumeWindowInsets(it)
                .imePadding()
                .verticalScroll(scrollState)
                .padding(16.dp),
            state = state,
            onAmountChange = onAmountChange,
            onSourceWalletSelect = onSourceWalletSelect,
            onTargetWalletSelect = onTargetWalletSelect,
            onAdminFeeChange = onFeeChange,
            onDateSelect = onDateSelect,
            onTimeSelect = onTimeSelect,
            onAddNewWalletClick = onAddWallet
        )
    }

    LaunchedEffect(eventFlow) {
        eventFlow.collectLatest {
            when (it) {
                is TransferTransactionViewModel.UiEvent.SaveTransaction -> onBackClick()
                is TransferTransactionViewModel.UiEvent.DeleteTransaction -> onBackClick()
                is TransferTransactionViewModel.UiEvent.ShowMessage -> snackbarHostState.showSnackbar(
                    it.message,
                )
            }
        }
    }
}