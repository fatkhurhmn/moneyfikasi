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
import dev.muffar.moneyfikasi.common_ui.component.button.bottom_bar.BottomBarSaveButton
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarMessage
import dev.muffar.moneyfikasi.common_ui.component.message.showMessage
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.debt.add_edit.component.AddEditDebtForm
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddEditDebtScreen(
    state: AddEditDebtState,
    eventFlow: SharedFlow<AddEditDebtViewModel.UiEvent>,
    onTypeChange: (DebtType) -> Unit,
    onNameChange: (String) -> Unit,
    onTotalAmountChange: (String) -> Unit,
    onDebtDateChange: (Long) -> Unit,
    onDueDateChange: (Long?) -> Unit,
    onNoteChange: (String) -> Unit,
    onWalletSelect: (Wallet) -> Unit,
    onAddNewWalletClick: () -> Unit,
    onSubmit: () -> Unit,
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
            BottomBarSaveButton(onSubmit)
        },
        snackbarHost = { SnackbarMessage(snackbarHostState) },
        contentWindowInsets = WindowInsets(0.dp)
    ) {
        AddEditDebtForm(
            modifier = Modifier.formModifier(it, scrollState),
            state = state,
            onTypeChange = onTypeChange,
            onNameChange = onNameChange,
            onTotalAmountChange = onTotalAmountChange,
            onDebtDateChange = onDebtDateChange,
            onDueDateChange = onDueDateChange,
            onNoteChange = onNoteChange,
            onWalletSelect = onWalletSelect,
            onAddNewWalletClick = onAddNewWalletClick
        )
    }

    LaunchedEffect(eventFlow) {
        eventFlow.collectLatest {
            when (it) {
                is AddEditDebtViewModel.UiEvent.SaveDebt -> onBackClick()
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
