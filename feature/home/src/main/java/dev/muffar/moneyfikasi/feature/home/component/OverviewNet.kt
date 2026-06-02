package dev.muffar.moneyfikasi.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.domain.model.TrendResult
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand

@Composable
fun OverviewNet(
    net: Double,
    trendResult: TrendResult,
    timePeriod: TimePeriod,
    isBalanceVisible: Boolean,
    onVisibilityClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.secondaryContainer,
                    )
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.label_net_balance),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )

            val balanceValue =
                if (isBalanceVisible) net.formatThousand()
                else stringResource(R.string.placeholder_hidden_balance)

            Text(
                text = balanceValue,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            BalanceTrendIndicator(trendResult = trendResult, timePeriod = timePeriod)
        }

        VisibilityButton(
            visibility = isBalanceVisible,
            onVisibilityClick = onVisibilityClick,
            modifier = Modifier.align(Alignment.TopEnd)
        )
    }
}
