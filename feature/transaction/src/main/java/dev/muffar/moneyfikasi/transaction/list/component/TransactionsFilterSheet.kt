package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import dev.muffar.moneyfikasi.common_ui.component.BottomSheetTitle
import dev.muffar.moneyfikasi.common_ui.component.CommonButton
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.CommonTabs
import dev.muffar.moneyfikasi.common_ui.component.DoubleOutlinedButton
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
    isCategoryFiltered: Boolean,
    wallets: List<Wallet>,
    isWalletFiltered: Boolean,
    onDismissRequest: () -> Unit,
    onApply: (TransactionFilter) -> Unit,
) {
    val filtersTab = listOf(
        "Category" to isCategoryFiltered,
        "Wallet" to isWalletFiltered
    )

    val pagerState = rememberPagerState { filtersTab.size }
    var selectedCategories by remember { mutableStateOf(filter.categories) }
    var selectedWallets by remember { mutableStateOf(filter.wallets) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    fun hideSheet(callback: () -> Unit) {
        scope.launch { sheetState.hide() }
        callback()
    }

    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        onDismissRequest = { hideSheet(onDismissRequest) },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        sheetGesturesEnabled = false
    ) {
        Column {
            BottomSheetTitle(stringResource(R.string.filter))
            CommonHorizontalDivider()
            CommonTabs(
                modifier = Modifier.weight(1f),
                tabs = filtersTab,
                pagerState = pagerState
            ) { index ->
                when (index) {
                    0 -> CategoriesFilterTab(
                        categories = categories,
                        selectedCategories = selectedCategories,
                        onSelectAll = {
                            selectedCategories = if (it) setOf() else categories.toSet()
                        },
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

                    1 -> WalletsFilterTab(
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

            CommonHorizontalDivider()
            DoubleOutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp),
                leftText = stringResource(R.string.cancel),
                rightText = stringResource(R.string.reset),
                onLeftClick = { hideSheet { onDismissRequest() } },
                onRightClick = {
                    hideSheet {
                        onApply(
                            filter.copy(
                                categories = categories.toSet(),
                                wallets = wallets.toSet()
                            )
                        )
                        onDismissRequest()
                    }
                }
            )
            CommonButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .padding(horizontal = 16.dp),
                text = stringResource(R.string.apply),
                onClick = {
                    hideSheet {
                        val mFilter = filter.copy(
                            categories = selectedCategories,
                            wallets = selectedWallets
                        )
                        onApply(mFilter)
                        onDismissRequest()
                    }
                }
            )
        }
    }
}