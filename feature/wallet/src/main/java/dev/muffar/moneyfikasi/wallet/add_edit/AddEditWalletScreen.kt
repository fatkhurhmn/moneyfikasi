package dev.muffar.moneyfikasi.wallet.add_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import dev.muffar.moneyfikasi.common_ui.component.CommonAlertDialog
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.wallet.add_edit.component.AddEditWalletAction
import dev.muffar.moneyfikasi.wallet.add_edit.component.AddEditWalletBottomSheet
import dev.muffar.moneyfikasi.wallet.add_edit.component.AddEditWalletForm
import dev.muffar.moneyfikasi.wallet.add_edit.component.WalletCard
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditWalletScreen(
    modifier: Modifier = Modifier,
    state: AddEditWalletState,
    eventFlow: SharedFlow<AddEditWalletViewModel.UiEvent>,
    onNameChange: (String) -> Unit,
    onBalanceChange: (String) -> Unit,
    onIconChange: (String) -> Unit,
    onColorChange: (Long) -> Unit,
    onIsActiveChange: () -> Unit,
    onShowBottomSheet: (AddEditWalletBottomSheet?) -> Unit,
    onShowAlert: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onDelete: () -> Unit,
    onBackClick: () -> Unit,
) {

    val sheetState = rememberModalBottomSheetState()
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
            AddEditWalletAction(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                isEdit = state.id != null,
                onSave = onSubmit,
                onDelete = { onShowAlert(true) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
                .verticalScroll(scrollState)
                .imePadding()
                .padding(16.dp)
        ) {
            WalletCard(
                name = state.name,
                color = state.color,
                icon = state.icon,
                balance = state.balance,
            )
            Spacer(modifier = Modifier.height(16.dp))
            AddEditWalletForm(
                id = state.id,
                name = state.name,
                balance = state.balance,
                icon = state.icon,
                color = state.color,
                isActive = state.isActive,
                onNameChange = onNameChange,
                onBalanceChange = onBalanceChange,
                onIconClick = {
                    onShowBottomSheet(AddEditWalletBottomSheet.ICON)
                },
                onColorClick = {
                    onShowBottomSheet(AddEditWalletBottomSheet.COLOR)
                },
                onIsActiveChange = onIsActiveChange,
            )
        }
        if (state.bottomSheetType != null) {
            ModalBottomSheet(
                onDismissRequest = { onShowBottomSheet(null) },
                sheetState = sheetState
            ) {
                AddEditWalletBottomSheet(
                    type = state.bottomSheetType,
                    onIconSelect = { icon ->
                        onIconChange(icon)
                        onShowBottomSheet(null)
                    },
                    onColorSelect = { color ->
                        onColorChange(color)
                    },
                    onDismiss = { onShowBottomSheet(null) }
                )
            }
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
}