package dev.muffar.moneyfikasi.transaction.transfer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.transaction.transfer.component.AddEditTransactionBottomSheet
import dev.muffar.moneyfikasi.transaction.transfer.component.TransferTransactionForm
import dev.muffar.moneyfikasi.transaction.transfer.component.TransferTransactionSheetType
import dev.muffar.moneyfikasi.utils.toFormattedDateTime
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale

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
    onTimeSelect: (Int, Int) -> Unit,
    onBackClick: () -> Unit,
    onCreateClick: () -> Unit,
    onAddWallet: () -> Unit,
    onShowBottomSheet: (TransferTransactionSheetType?) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

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

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.transfer),
                onBackClick = onBackClick
            )
        },
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
                .verticalScroll(scrollState)
                .imePadding()
                .padding(16.dp)
        ) {
            TransferTransactionForm(
                amount = state.amount,
                adminFee = state.fee,
                date = state.date.toFormattedDateTime("MMM, dd yyyy"),
                time = String.format(Locale.getDefault(), "%02d:%02d", state.hour, state.minute),
                sourceWallet = state.sourceWallet,
                targetWallet = state.targetWallet,
                onAmountChange = onAmountChange,
                onOriginWalletClick = { onShowBottomSheet(TransferTransactionSheetType.SOURCE_WALLET) },
                onDestinationWalletClick = { onShowBottomSheet(TransferTransactionSheetType.TARGET_WALLET) },
                onAdminFeeChange = onFeeChange,
                onDateClick = { onShowBottomSheet(TransferTransactionSheetType.DATE) },
                onTimeClick = { onShowBottomSheet(TransferTransactionSheetType.TIME) },
                onTransferClick = onCreateClick
            )

            if (state.bottomSheetType != null) {
                ModalBottomSheet(
                    onDismissRequest = { onShowBottomSheet(null) },
                    sheetState = sheetState
                ) {
                    AddEditTransactionBottomSheet(
                        type = state.bottomSheetType,
                        wallets = state.wallets,
                        date = state.date,
                        hour = state.hour,
                        minute = state.minute,
                        onDateSelect = onDateSelect,
                        onTimeSelect = onTimeSelect,
                        onDismiss = { onShowBottomSheet(null) },
                        onAddWallet = onAddWallet,
                        onSourceWalletSelect = onSourceWalletSelect,
                        onTargetWalletSelect = onTargetWalletSelect
                    )
                }
            }
        }
    }
}