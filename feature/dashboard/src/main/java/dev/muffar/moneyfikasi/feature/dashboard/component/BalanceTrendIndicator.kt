package dev.muffar.moneyfikasi.feature.dashboard.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.domain.model.TimePeriod
import dev.muffar.moneyfikasi.resource.R
import kotlin.math.absoluteValue

@Composable
fun BalanceTrendIndicator(
    trend: Double,
    timePeriod: TimePeriod,
    modifier: Modifier = Modifier,
) {
    val isPositive = trend >= 0
    val icon = if (isPositive) Icons.Rounded.ArrowUpward else Icons.Rounded.ArrowDownward

    val trendMessage = when (timePeriod) {
        TimePeriod.DAILY -> stringResource(R.string.trend_vs_yesterday, trend.absoluteValue.toInt())
        TimePeriod.WEEKLY -> stringResource(
            R.string.trend_vs_last_month,
            trend.absoluteValue.toInt()
        )

        TimePeriod.MONTHLY -> stringResource(
            R.string.trend_vs_last_month,
            trend.absoluteValue.toInt()
        )

        else -> ""
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = trendMessage,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp
            ),
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
        )
    }
}