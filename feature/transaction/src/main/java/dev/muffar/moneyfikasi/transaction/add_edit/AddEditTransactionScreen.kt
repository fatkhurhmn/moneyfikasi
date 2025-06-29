package dev.muffar.moneyfikasi.transaction.add_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.transaction.add_edit.component.AddEditTransactionBottomSheet
import dev.muffar.moneyfikasi.transaction.add_edit.component.AddEditTransactionButton
import dev.muffar.moneyfikasi.transaction.add_edit.component.AddEditTransactionForm
import dev.muffar.moneyfikasi.transaction.add_edit.component.AddEditTransactionSheetType
import dev.muffar.moneyfikasi.utils.toFormattedDateTime
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale

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
    onTimeSelect: (Int, Int) -> Unit,
    onNoteChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onCreateClick: () -> Unit,
    onAddWallet: () -> Unit,
    onAddCategory: (CategoryType) -> Unit,
    onShowBottomSheet: (AddEditTransactionSheetType?) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

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

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.offset(y = 40.dp)
            )
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
                date = state.date.toFormattedDateTime("MMM, dd yyyy"),
                time = String.format(Locale.getDefault(), "%02d:%02d", state.hour, state.minute),
                onAmountChange = onAmountChange,
                onNoteChange = onNoteChange,
                onCategoryClick = { onShowBottomSheet(AddEditTransactionSheetType.CATEGORY) },
                onWalletClick = { onShowBottomSheet(AddEditTransactionSheetType.WALLET) },
                onDateClick = { onShowBottomSheet(AddEditTransactionSheetType.DATE) },
                onTimeClick = { onShowBottomSheet(AddEditTransactionSheetType.TIME) }
            )

            Spacer(Modifier.height(32.dp))

            AddEditTransactionButton(onCreateClick)

            if (state.bottomSheetType != null) {
                ModalBottomSheet(
                    onDismissRequest = { onShowBottomSheet(null) },
                    sheetState = sheetState
                ) {
                    AddEditTransactionBottomSheet(
                        type = state.bottomSheetType,
                        categories = state.categories,
                        wallets = state.wallets,
                        date = state.date,
                        hour = state.hour,
                        minute = state.minute,
                        onCategorySelect = onCategorySelect,
                        onWalletSelect = onWalletSelect,
                        onDateSelect = onDateSelect,
                        onTimeSelect = onTimeSelect,
                        onDismiss = { onShowBottomSheet(null) },
                        onAddWallet = onAddWallet,
                        onAddCategory = {
                            val type = if (state.type == TransactionType.INCOME) {
                                CategoryType.INCOME
                            } else {
                                CategoryType.EXPENSE
                            }

                            onAddCategory(type)
                        }
                    )
                }
            }
        }
    }
}