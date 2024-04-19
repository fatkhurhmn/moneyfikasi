package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.CommonTabs
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.domain.utils.TransactionFilter
import dev.muffar.moneyfikasi.resource.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionsFilterSheet(
    modifier: Modifier = Modifier,
    filter: TransactionFilter,
    categories: List<Category>,
    wallets: List<Wallet>,
    onClose: () -> Unit,
    onFilterChanged: (TransactionFilter) -> Unit,
) {
    val filtersTab = listOf("Date Range", "Category", "Wallet")
    val pagerState = rememberPagerState { filtersTab.size }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.filter),
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 20.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        CommonTabs(
            modifier = modifier,
            tabs = filtersTab,
            pagerState = pagerState
        ) { index ->
            when (index) {
                0 -> DateRangeFilterTab(
                    filter = filter,
                    onFilterSelect = {
                        onFilterChanged(it)
                        if (it != TransactionFilter.CUSTOM) {
                            onClose()
                        }
                    }
                )

                1 -> CategoriesFilterTab(categories = categories)

                2 -> WalletsFilterTab(wallets = wallets)
            }
        }
    }
}