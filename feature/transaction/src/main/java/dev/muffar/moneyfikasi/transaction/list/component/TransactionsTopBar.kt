package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
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
            TransactionsTopBarButton(
                painter = painterResource(R.drawable.ic_search),
                onClick = onSearchClick
            )
            Spacer(modifier = Modifier.width(8.dp))
            TransactionsTopBarButton(
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

@Composable
fun TransactionsTopBarButton(
    painter: Painter,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .size(40.dp)
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
}
