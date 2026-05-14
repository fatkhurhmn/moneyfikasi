package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.top_bar.TopBarButton
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
            TopBarButton(
                painter = painterResource(R.drawable.ic_search),
                onClick = onSearchClick
            )
            Spacer(modifier = Modifier.width(8.dp))
            TopBarButton(
                painter = painterResource(R.drawable.ic_date),
                onClick = onChooseDateClick
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterIcon(
                isFilterApplied = showFilterBadge,
                onClick = onFilterClick,
            )
        }
    )
}

