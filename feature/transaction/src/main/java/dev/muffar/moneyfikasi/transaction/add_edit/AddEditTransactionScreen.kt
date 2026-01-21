package dev.muffar.moneyfikasi.transaction.add_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.transaction.add_edit.component.AddEditTransactionButton
import dev.muffar.moneyfikasi.transaction.add_edit.component.AddEditTransactionForm
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTransactionScreen(
    modifier: Modifier = Modifier,
    state: AddEditTransactionState,
    eventFlow: SharedFlow<AddEditTransactionViewModel.UiEvent>,
    onAmountChange: (String) -> Unit,
    onCategorySelect: (Category) -> Unit,
    onWalletSelect: (Wallet) -> Unit,
    onDateSelect: (Long) -> Unit,
    onTimeSelect: (Pair<Int, Int>) -> Unit,
    onNoteChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onCreateClick: () -> Unit,
    onAddNewWalletClick: () -> Unit,
    onAddNewCategoryClick: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            CommonTopAppBar(
                title = state.type.value,
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
            AddEditTransactionForm(
                amount = state.amount,
                note = state.note,
                category = state.category,
                wallet = state.wallet,
                date = state.date,
                time = state.hour to state.minute,
                categoryOptions = state.categories,
                walletOptions = state.walletOptions,
                onAmountChange = onAmountChange,
                onNoteChange = onNoteChange,
                onCategorySelect = onCategorySelect,
                onAddNewCategoryClick = onAddNewCategoryClick,
                onWalletSelect = onWalletSelect,
                onAddNewWalletClick = onAddNewWalletClick,
                onDateSelect = onDateSelect,
                onTimeSelect = onTimeSelect
            )

            Spacer(Modifier.height(32.dp))

            AddEditTransactionButton(onCreateClick)
        }
    }

    LaunchedEffect(eventFlow) {
        eventFlow.collectLatest {
            when (it) {
                is AddEditTransactionViewModel.UiEvent.SaveTransaction -> onBackClick()
                is AddEditTransactionViewModel.UiEvent.DeleteTransaction -> onBackClick()
                is AddEditTransactionViewModel.UiEvent.ShowMessage -> snackbarHostState.showSnackbar(
                    it.message,
                )
            }
        }
    }
}