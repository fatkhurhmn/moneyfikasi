package dev.muffar.moneyfikasi.debt.add_edit

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.ModifierExt.formModifier
import dev.muffar.moneyfikasi.common_ui.component.button.bottom_bar.BottomBarAddEditButton
import dev.muffar.moneyfikasi.common_ui.component.dialog.CommonAlertDialog
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarMessage
import dev.muffar.moneyfikasi.common_ui.component.message.showMessage
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.debt.add_edit.component.AddEditDebtForm
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddEditDebtScreen(
    state: AddEditDebtState,
    eventFlow: SharedFlow<AddEditDebtViewModel.UiEvent>,
    onNameChange: (String) -> Unit,
    onTotalAmountChange: (String) -> Unit,
    onPaidAmountChange: (String) -> Unit,
    onDueDateChange: (Long?) -> Unit,
    onNoteChange: (String) -> Unit,
    onWalletSelect: (Wallet) -> Unit,
    onAddNewWalletClick: () -> Unit,
    onShowAlert: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onDelete: () -> Unit,
    onBackClick: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.title_debt),
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            BottomBarAddEditButton(
                isEdit = state.id != null,
                onSave = onSubmit,
                onDelete = { onShowAlert(true) }
            )
        },
        snackbarHost = { SnackbarMessage(snackbarHostState) },
        contentWindowInsets = WindowInsets(0.dp)
    ) {
        AddEditDebtForm(
            modifier = Modifier.formModifier(it, scrollState),
            state = state,
            onNameChange = onNameChange,
            onTotalAmountChange = onTotalAmountChange,
            onPaidAmountChange = onPaidAmountChange,
            onDueDateChange = onDueDateChange,
            onNoteChange = onNoteChange,
            onWalletSelect = onWalletSelect,
            onAddNewWalletClick = onAddNewWalletClick
        )
    }

    if (state.showAlert) {
        CommonAlertDialog(
            title = stringResource(R.string.title_delete_debt),
            message = stringResource(R.string.msg_delete_debt_confirmation),
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
                is AddEditDebtViewModel.UiEvent.SaveDebt -> onBackClick()
                is AddEditDebtViewModel.UiEvent.DeleteDebt -> onBackClick()
                is AddEditDebtViewModel.UiEvent.ShowMessage -> {
                    snackbarHostState.showMessage(
                        context.applicationContext.getString(it.messageResId),
                        it.type
                    )
                }
            }
        }
    }
}
