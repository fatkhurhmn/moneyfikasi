package dev.muffar.moneyfikasi.wallet.add_edit

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.button.bottom_bar.BottomBarAddEditButton
import dev.muffar.moneyfikasi.common_ui.component.dialog.CommonAlertDialog
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarMessage
import dev.muffar.moneyfikasi.common_ui.component.message.showMessage
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.wallet.add_edit.component.AddEditWalletContent
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
    val context = LocalContext.current
    val isEditMode = state.id != null
    val title = "${stringResource(if (isEditMode) R.string.action_edit else R.string.action_create)} " +
            stringResource(R.string.label_wallet)

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = title,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            BottomBarAddEditButton(
                isEdit = isEditMode,
                onSave = onSubmit,
                onDelete = { onShowAlert(true) }
            )
        },
        snackbarHost = { SnackbarMessage(snackbarHostState) }
    ) { paddingValues ->
        AddEditWalletContent(
            paddingValues = paddingValues,
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
            title = stringResource(R.string.title_delete_wallet),
            message = stringResource(R.string.msg_delete_wallet_confirmation),
            positiveText = stringResource(R.string.action_delete),
            negativeText = stringResource(R.string.action_cancel),
            onDismiss = { onShowAlert(false) },
            onConfirm = {
                onDelete()
                onShowAlert(false)
            }
        )
    }

    LaunchedEffect(eventFlow) {
        eventFlow.collectLatest {
            when (it) {
                is AddEditWalletViewModel.UiEvent.SaveWallet -> onBackClick()
                is AddEditWalletViewModel.UiEvent.DeleteWallet -> onBackClick()
                is AddEditWalletViewModel.UiEvent.ShowMessage -> snackbarHostState.showMessage(
                    context.applicationContext.getString(it.messageResId),
                    it.type
                )
            }
        }
    }
}
