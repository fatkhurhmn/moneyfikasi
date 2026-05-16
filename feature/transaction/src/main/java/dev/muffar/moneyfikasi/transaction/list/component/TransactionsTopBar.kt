package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        titleSize = 20.sp,
        action = {
            TopBarButton(
                imageVector = Icons.Rounded.Search,
                onClick = onSearchClick
            )
            Spacer(modifier = Modifier.width(8.dp))
            TopBarButton(
                imageVector = Icons.Rounded.CalendarToday,
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

