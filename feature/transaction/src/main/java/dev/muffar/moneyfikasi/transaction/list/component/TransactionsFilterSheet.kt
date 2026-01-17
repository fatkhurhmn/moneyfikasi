package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import dev.muffar.moneyfikasi.common_ui.component.CommonTabs
import dev.muffar.moneyfikasi.common_ui.component.DateRangeSheet
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.model.TransactionFilter
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.utils.TimePeriod
import dev.muffar.moneyfikasi.resource.R

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TransactionsFilterSheet(
    filter: TransactionFilter,
    categories: List<Category>,
    isCategoryFiltered: Boolean,
    wallets: List<Wallet>,
    isWalletFiltered: Boolean,
    onApply: (TransactionFilter) -> Unit,
    onClose: () -> Unit,
) {
    val filtersTab = listOf(
        "Date Range" to false,
        "Category" to isCategoryFiltered,
        "Wallet" to isWalletFiltered
    )
    val pagerState = rememberPagerState { filtersTab.size }

    val dateRangeSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showDateRangeSheet by remember { mutableStateOf(false) }

    var timePeriod by remember { mutableStateOf(filter.timePeriod) }
    var startDate by remember { mutableLongStateOf(if (filter.timePeriod == TimePeriod.CUSTOM) filter.dateRange.start else 0L) }
    var endDate by remember { mutableLongStateOf(if (filter.timePeriod == TimePeriod.CUSTOM) filter.dateRange.end else 0L) }
    var selectedCategories by remember { mutableStateOf(filter.categories) }
    var selectedWallets by remember { mutableStateOf(filter.wallets) }

    Column {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.filter),
            style = MaterialTheme.typography.titleLarge
        )
        CommonTabs(
            modifier = Modifier.weight(1f),
            tabs = filtersTab,
            pagerState = pagerState
        ) { index ->
            when (index) {
                0 -> DateRangeFilterTab(
                    filter = timePeriod,
                    startDateMillis = startDate,
                    endDateMillis = endDate,
                    onFilterSelect = {
                        if (it == TimePeriod.CUSTOM) {
                            showDateRangeSheet = true
                        } else {
                            timePeriod = it
                        }
                    },
                )

                1 -> CategoriesFilterTab(
                    categories = categories,
                    selectedCategories = selectedCategories,
                    onSelectAll = { selectedCategories = if (it) setOf() else categories.toSet() },
                    onSelectAllSameType = { isAllSameTypeSelected, categoriesByType ->
                        if (isAllSameTypeSelected) {
                            selectedCategories =
                                selectedCategories.filter { it !in categoriesByType }.toSet()
                        } else {
                            selectedCategories += categoriesByType
                        }
                    },
                    onSelect = { item ->
                        if (item in selectedCategories) {
                            selectedCategories -= item
                        } else {
                            selectedCategories += item
                        }
                    }
                )

                2 -> WalletsFilterTab(
                    wallets = wallets,
                    selectedWallets = selectedWallets,
                    onSelectAll = { isSelectAll ->
                        selectedWallets = if (isSelectAll) setOf() else wallets.toSet()
                    },
                    onSelect = { item ->
                        if (item in selectedWallets) {
                            selectedWallets -= item
                        } else {
                            selectedWallets += item
                        }
                    }
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(onClick = onClose) {
                Text(
                    text = stringResource(R.string.cancel),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Button(
                onClick = {
                    val filter = TransactionFilter(
                        timePeriod = timePeriod,
                        dateRange = DateRange(startDate, endDate),
                        categories = selectedCategories,
                        wallets = selectedWallets
                    )
                    onApply(filter)
                    onClose()
                }
            ) {
                Text(
                    text = stringResource(R.string.apply),
                    style = MaterialTheme.typography.titleMedium
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
                startDateMillis = if (filter.timePeriod == TimePeriod.CUSTOM) filter.dateRange.start else null,
                endDateMillis = if (filter.timePeriod == TimePeriod.CUSTOM) filter.dateRange.end else null,
                onDateChange = { start, end ->
                    startDate = start
                    endDate = end
                    timePeriod = TimePeriod.CUSTOM
                },
                onClose = { showDateRangeSheet = false },
            )
        }
    }
}