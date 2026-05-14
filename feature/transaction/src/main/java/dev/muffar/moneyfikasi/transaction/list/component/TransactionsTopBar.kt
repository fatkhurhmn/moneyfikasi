package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.button.TopBarIconButton
import dev.muffar.moneyfikasi.resource.R

@Composable
fun TransactionsTopBar(
    showFilterBadge: Boolean,
    onSearchClick: () -> Unit,
    onChooseDateClick: () -> Unit,
    onFilterClick: () -> Unit,
) {
    CommonTopAppBar(
        title = stringResource(R.string.transactions),
        showBackButton = false,
        action = {
            TopBarIconButton(
                painter = painterResource(R.drawable.ic_search),
                onClick = onSearchClick
            )
            TopBarIconButton(
                painter = painterResource(R.drawable.ic_date),
                onClick = onChooseDateClick
            )
            FilterIcon(
                isFilterApplied = showFilterBadge,
                onClick = onFilterClick,
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    )
}