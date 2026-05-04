package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.BottomSheetTitle
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.TransactionFilter
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TransactionsFilterSheet(
    filter: TransactionFilter,
    categories: List<Category>,
    wallets: List<Wallet>,
    isFilterApplied: Boolean,
    onDismissRequest: () -> Unit,
    onResetFilter: () -> Unit,
    onApply: (TransactionFilter) -> Unit,
) {
    var selectedCategories by remember { mutableStateOf(if (!isFilterApplied) categories.toSet() else filter.categories) }
    var selectedWallets by remember { mutableStateOf(if (!isFilterApplied) wallets.toSet() else filter.wallets) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope(

    )
    val hideSheet = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismissRequest()
            }
        }
    }

    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        sheetGesturesEnabled = false
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            topBar = {
                BottomSheetTitle(stringResource(R.string.filter))
            },
            bottomBar = {
                FilterSheetButton(
                    onCancelClick = { hideSheet() },
                    onResetClick = {
                        hideSheet()
                        onResetFilter()
                    },
                    onApplyClick = {
                        hideSheet()
                        val mFilter = filter.copy(
                            categories = selectedCategories,
                            wallets = selectedWallets
                        )
                        onApply(mFilter)
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                FilterWalletSection(
                    wallets = wallets,
                    selectedWallets = selectedWallets,
                    onSelectAll = { selectedWallets = it },
                    onSelect = {
                        if (it in selectedWallets) {
                            selectedWallets -= it
                        } else {
                            selectedWallets += it
                        }
                    }
                )

                FilterCategorySection(
                    categories = categories,
                    selectedCategories = selectedCategories,
                    onSelectAll = { selectedCategories = it },
                    onSelectAllIncome = { selectedCategories = it },
                    onSelectAllExpense = { selectedCategories = it },
                    onSelect = {
                        if (it in selectedCategories) {
                            selectedCategories -= it
                        } else {
                            selectedCategories += it
                        }
                    }
                )
            }
        }
    }
}