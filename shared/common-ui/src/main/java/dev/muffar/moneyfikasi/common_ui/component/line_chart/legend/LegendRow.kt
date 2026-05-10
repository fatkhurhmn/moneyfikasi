package dev.muffar.moneyfikasi.common_ui.component.line_chart.legend

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.resource.R

@Composable
fun LegendRow(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LegendItem(
            color = MoneyfikasiTheme.financeColors.income,
            label = stringResource(R.string.income),
        )
        LegendItem(
            color = MoneyfikasiTheme.financeColors.expense,
            label = stringResource(R.string.expense),
        )
    }
}