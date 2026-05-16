package dev.muffar.moneyfikasi.statistic.main.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.top_bar.TopBarButton
import dev.muffar.moneyfikasi.resource.R

@Composable
fun StatisticTopBar(
    onFilterClick: () -> Unit,
) {
    CommonTopAppBar(
        title = stringResource(R.string.statistic_menu),
        showBackButton = false,
        titleSize = 20.sp,
        action = {
            TopBarButton(
                imageVector = Icons.Rounded.CalendarToday,
                onClick = onFilterClick
            )
        },
    )
}