package dev.muffar.moneyfikasi.statistic.main.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand

@Composable
fun StatisticOverviewSection(
    income: Double,
    expense: Double,
    total: Double,
) {
    val financeColors = MoneyfikasiTheme.financeColors
    PrimaryCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
        ) {
            StatisticSectionLabel(
                label = stringResource(R.string.overview),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            StatisticOverviewRow(
                label = stringResource(R.string.income),
                amount = income.formatThousand().let { if (income > 0) "+$it" else it },
                color = financeColors.income
            )
            StatisticOverviewRow(
                label = stringResource(R.string.expense),
                amount = expense.formatThousand().let { if (expense > 0) "-$it" else it },
                color = financeColors.expense
            )
            StatisticOverviewRow(
                label = stringResource(R.string.net),
                amount = total.formatThousand(),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
