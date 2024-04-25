package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.CommonTabs
import dev.muffar.moneyfikasi.common_ui.component.DateRangeSheet
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.utils.TransactionDateFilter
import dev.muffar.moneyfikasi.resource.R

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TransactionsFilterSheet(
    modifier: Modifier = Modifier,
    filter: TransactionDateFilter,
    categories: List<Category>,
    wallets: List<Wallet>,
    selectedCategories: Set<Category>,
    selectedWallets: Set<Wallet>,
    startDateMillis: Long,
    endDateMillis: Long,
    onSave: (
        filter: TransactionDateFilter,
        startDateMillis: Long,
        endDateMillis: Long,
        categories: Set<Category>,
        wallets: Set<Wallet>,
    ) -> Unit,
    onClose: () -> Unit,
) {
    val filtersTab = listOf("Date Range", "Category", "Wallet")
    val pagerState = rememberPagerState { filtersTab.size }

    val dateRangeSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showDateRangeSheet by remember { mutableStateOf(false) }

    var mFilter by remember { mutableStateOf(filter) }
    var selectedStartDate by remember { mutableLongStateOf(if (filter == TransactionDateFilter.CUSTOM) startDateMillis else 0L) }
    var selectedEndDate by remember { mutableLongStateOf(if (filter == TransactionDateFilter.CUSTOM) endDateMillis else 0L) }
    var mSelectedCategories by remember { mutableStateOf(selectedCategories) }
    var mSelectedWallets by remember { mutableStateOf(selectedWallets) }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.filter),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 20.sp
                ),
                modifier = Modifier
            )

            TextButton(
                onClick = {
                    onSave(
                        mFilter,
                        selectedStartDate,
                        selectedEndDate,
                        mSelectedCategories,
                        mSelectedWallets
                    )
                    onClose()
                }
            ) {
                Text(text = stringResource(R.string.save), fontSize = 18.sp)
            }
        }

        CommonTabs(
            modifier = modifier,
            tabs = filtersTab,
            pagerState = pagerState
        ) { index ->
            when (index) {
                0 -> DateRangeFilterTab(
                    filter = mFilter,
                    startDateMillis = selectedStartDate,
                    endDateMillis = selectedEndDate,
                    onFilterSelect = {
                        if (it == TransactionDateFilter.CUSTOM) {
                            showDateRangeSheet = true
                        } else {
                            mFilter = it
                        }
                    },
                )

                1 -> CategoriesFilterTab(
                    categories = categories,
                    selectedCategories = mSelectedCategories,
                    onSelectAll = { mSelectedCategories = if (it) setOf() else categories.toSet() },
                    onSelectAllSameType = { isAllSameTypeSelected, categoriesByType ->
                        if (isAllSameTypeSelected) {
                            mSelectedCategories =
                                mSelectedCategories.filter { it !in categoriesByType }.toSet()
                        } else {
                            mSelectedCategories += categoriesByType
                        }
                    },
                    onSelect = { item ->
                        if (item in mSelectedCategories) {
                            mSelectedCategories -= item
                        } else {
                            mSelectedCategories += item
                        }
                    }
                )

                2 -> WalletsFilterTab(
                    wallets = wallets,
                    selectedWallets = mSelectedWallets,
                    onSelectAll = { isSelectAll ->
                        mSelectedWallets = if (isSelectAll) setOf() else wallets.toSet()
                    },
                    onSelect = { item ->
                        if (item in mSelectedWallets) {
                            mSelectedWallets -= item
                        } else {
                            mSelectedWallets += item
                        }
                    }
                )
            }
        }
    }

    if (showDateRangeSheet) {
        ModalBottomSheet(
            onDismissRequest = { showDateRangeSheet = false },
            sheetState = dateRangeSheetState
        ) {
            DateRangeSheet(
                startDateMillis = if (filter == TransactionDateFilter.CUSTOM) startDateMillis else null,
                endDateMillis = if (filter == TransactionDateFilter.CUSTOM) endDateMillis else null,
                onDateChange = { start, end ->
                    selectedStartDate = start
                    selectedEndDate = end
                    mFilter = TransactionDateFilter.CUSTOM
                },
                onClose = { showDateRangeSheet = false },
            )
        }
    }
}