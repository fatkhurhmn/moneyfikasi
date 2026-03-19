package dev.muffar.moneyfikasi.wallet.add_edit

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonAlertDialog
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.wallet.add_edit.component.AddEditWalletButton
import dev.muffar.moneyfikasi.wallet.add_edit.component.AddEditWalletForm
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditWalletScreen(
    state: AddEditWalletState,
    eventFlow: SharedFlow<AddEditWalletViewModel.UiEvent>,
    onNameChange: (String) -> Unit,
    onBalanceChange: (String) -> Unit,
    onIconSelect: (String) -> Unit,
    onColorSelect: (Long) -> Unit,
    onWalletActive: () -> Unit,
    onShowAlert: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onDelete: () -> Unit,
    onBackClick: () -> Unit,
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    LaunchedEffect(eventFlow) {
        eventFlow.collectLatest {
            when (it) {
                is AddEditWalletViewModel.UiEvent.SaveWallet -> onBackClick()
                is AddEditWalletViewModel.UiEvent.DeleteWallet -> onBackClick()
                is AddEditWalletViewModel.UiEvent.ShowMessage -> snackbarHostState.showSnackbar(it.message)
            }
        }
    }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.wallet),
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            AddEditWalletButton(
                isEdit = state.id != null,
                onSave = onSubmit,
                onDelete = { onShowAlert(true) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        AddEditWalletForm(
            modifier = Modifier
                .padding(it)
                .consumeWindowInsets(it)
                .imePadding()
                .verticalScroll(scrollState)
                .padding(16.dp),
            state = state,
            onNameChange = onNameChange,
            onBalanceChange = onBalanceChange,
            onIconSelect = onIconSelect,
            onColorSelect = onColorSelect,
            onWalletActive = onWalletActive,
        )
    }

    if (state.showAlert) {
        CommonAlertDialog(
            title = stringResource(R.string.delete_wallet),
            message = stringResource(R.string.delete_wallet_confirmation),
            positiveText = stringResource(R.string.delete),
            negativeText = stringResource(R.string.cancel),
            onDismiss = { onShowAlert(false) },
            onConfirm = {
                onDelete()
                onShowAlert(false)
            }
        )
    }
}