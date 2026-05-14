package dev.muffar.moneyfikasi.statistic.main.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.button.TopBarIconButton
import dev.muffar.moneyfikasi.resource.R

@Composable
fun StatisticTopBar(
    onFilterClick: () -> Unit,
) {
    CommonTopAppBar(
        title = stringResource(R.string.statistic_menu),
        showBackButton = false,
        action = {
            TopBarIconButton(
                painter = painterResource(R.drawable.ic_date),
                onClick = onFilterClick
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    )
}