package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.resource.R

@Composable
fun TransactionsTopBar(
    showFilterBadge: Boolean,
    onFilterClick: () -> Unit,
) {
    CommonTopAppBar(
        title = stringResource(R.string.transactions),
        showBackButton = false,
        action = {
            FilterIcon(
                isFilterApplied = showFilterBadge,
                onFilterClick = onFilterClick,
            )
        }
    )
}