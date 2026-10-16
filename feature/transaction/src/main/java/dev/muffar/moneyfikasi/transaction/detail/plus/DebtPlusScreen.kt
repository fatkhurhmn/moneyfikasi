package dev.muffar.moneyfikasi.transaction.detail.plus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.ModifierExt.formModifier
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonButton
import dev.muffar.moneyfikasi.common_ui.component.dialog.CommonAlertDialog
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarMessage
import dev.muffar.moneyfikasi.common_ui.component.message.showMessage
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.top_bar.TopBarButton
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.transaction.detail.plus.component.DebtPlusForm
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DebtPlusScreen(
    state: DebtPlusState,
    eventFlow: SharedFlow<DebtPlusViewModel.UiEvent>,
    onTabSelected: (Int) -> Unit,
    onAmountChange: (String) -> Unit,
    onWalletSelect: (Wallet) -> Unit,
    onAddNewWalletClick: () -> Unit,
    onDateSelect: (Long) -> Unit,
    onSubmit: () -> Unit,
    onShowDeleteAlert: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val isEditMode = state.editingHistoryId != null
    val confirmRes = if (state.isPaymentTab) {
        if (state.debt?.type == DebtType.DEBT) {
            R.string.label_pay_debt
        } else {
            R.string.label_collect_debt
        }
    } else {
        R.string.action_save
    }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.title_debt),
                onBackClick = onBackClick,
                action = {
                    if (isEditMode) {
                        TopBarButton(
                            imageVector = Icons.Rounded.Delete,
                            color = MaterialTheme.colorScheme.error,
                            onClick = { onShowDeleteAlert(true) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .imePadding()
                    .navigationBarsPadding()
            ) {
                CommonHorizontalDivider()
                CommonButton(
                    text = stringResource(confirmRes),
                    enabled = !state.isRecording,
                    onClick = onSubmit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        },
        snackbarHost = { SnackbarMessage(snackbarHostState) },
        contentWindowInsets = WindowInsets(0.dp)
    ) {
        DebtPlusForm(
            modifier = Modifier.formModifier(it, scrollState),
            state = state,
            showTabs = !isEditMode,
            onTabSelected = onTabSelected,
            onAmountChange = onAmountChange,
            onWalletSelect = onWalletSelect,
            onAddNewWalletClick = onAddNewWalletClick,
            onDateSelect = onDateSelect
        )
    }

    if (state.showDeleteAlert) {
        CommonAlertDialog(
            title = stringResource(R.string.title_delete_history),
            message = stringResource(R.string.msg_delete_history_confirmation),
            positiveText = stringResource(R.string.action_delete),
            negativeText = stringResource(R.string.action_cancel),
            onDismiss = { onShowDeleteAlert(false) },
            onConfirm = {
                onDelete()
                onShowDeleteAlert(false)
            }
        )
    }

    LaunchedEffect(eventFlow) {
        eventFlow.collectLatest {
            when (it) {
                is DebtPlusViewModel.UiEvent.SaveDebtPlus -> onBackClick()
                is DebtPlusViewModel.UiEvent.DeleteDebtHistory -> onBackClick()
                is DebtPlusViewModel.UiEvent.NavigateBack -> onBackClick()
                is DebtPlusViewModel.UiEvent.ShowMessage -> {
                    snackbarHostState.showMessage(
                        context.applicationContext.getString(it.messageResId),
                        it.type
                    )
                }
            }
        }
    }
}
