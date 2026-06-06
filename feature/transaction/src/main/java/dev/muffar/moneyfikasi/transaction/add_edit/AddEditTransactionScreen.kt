package dev.muffar.moneyfikasi.transaction.add_edit

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.ModifierExt.formModifier
import dev.muffar.moneyfikasi.common_ui.component.button.bottom_bar.BottomBarSaveButton
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarMessage
import dev.muffar.moneyfikasi.common_ui.component.message.showMessage
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.transaction.add_edit.component.AddEditTransactionForm
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEditTransactionScreen(
    state: AddEditTransactionState,
    eventFlow: SharedFlow<AddEditTransactionViewModel.UiEvent>,
    onTypeChange: (TransactionType) -> Unit,
    onAmountChange: (String) -> Unit,
    onCategorySelect: (Category) -> Unit,
    onWalletSelect: (Wallet) -> Unit,
    onDateSelect: (Long) -> Unit,
    onTimeSelect: (Pair<Int, Int>) -> Unit,
    onNoteChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onAddNewWalletClick: () -> Unit,
    onAddNewCategoryClick: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    Scaffold(
        snackbarHost = {
            SnackbarMessage(state = snackbarHostState)
        },
        topBar = {
            CommonTopAppBar(
                title = if (state.type == TransactionType.EXPENSE) {
                    stringResource(id = R.string.label_expense)
                } else {
                    stringResource(id = R.string.label_income)
                },
                onBackClick = onBackClick
            )
        },
        bottomBar = { BottomBarSaveButton(onSaveClick) },
        contentWindowInsets = WindowInsets(0.dp),
    ) {
        AddEditTransactionForm(
            modifier = Modifier.formModifier(it, scrollState),
            state = state,
            onTypeChange = onTypeChange,
            onAmountChange = onAmountChange,
            onNoteChange = onNoteChange,
            onCategorySelect = onCategorySelect,
            onAddNewCategoryClick = onAddNewCategoryClick,
            onWalletSelect = onWalletSelect,
            onAddNewWalletClick = onAddNewWalletClick,
            onDateSelect = onDateSelect,
            onTimeSelect = onTimeSelect,
        )
    }

    LaunchedEffect(eventFlow) {
        eventFlow.collectLatest {
            when (it) {
                is AddEditTransactionViewModel.UiEvent.SaveTransaction -> onBackClick()
                is AddEditTransactionViewModel.UiEvent.DeleteTransaction -> onBackClick()
                is AddEditTransactionViewModel.UiEvent.ShowMessage -> snackbarHostState.showMessage(
                    it.message,
                    it.type
                )
            }
        }
    }
}