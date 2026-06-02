package dev.muffar.moneyfikasi.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
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
import dev.muffar.moneyfikasi.domain.model.TrendResult
import dev.muffar.moneyfikasi.domain.model.TrendType
import dev.muffar.moneyfikasi.resource.R
import kotlin.math.absoluteValue

@Composable
fun BalanceTrendIndicator(
    trendResult: TrendResult,
    timePeriod: TimePeriod,
    modifier: Modifier = Modifier,
) {
    val icon = when (trendResult.type) {
        TrendType.UP -> Icons.AutoMirrored.Default.TrendingUp
        TrendType.DOWN -> Icons.AutoMirrored.Default.TrendingDown
        else -> null
    }

    val resultMessage = when (trendResult.type) {
        TrendType.UP, TrendType.DOWN -> stringResource(
            R.string.format_trend_percentage,
            trendResult.percentage.absoluteValue.toInt()
        )

        TrendType.NEW_GROWTH -> stringResource(R.string.label_new_growth)
        TrendType.NEW_LOSS -> stringResource(R.string.label_new_loss)
        TrendType.NEUTRAL -> stringResource(R.string.label_no_change)
    }

    val trendMessage = when (timePeriod) {
        TimePeriod.DAILY -> stringResource(R.string.msg_trend_vs_yesterday, resultMessage)
        TimePeriod.WEEKLY -> stringResource(R.string.msg_trend_vs_last_week, resultMessage)
        TimePeriod.MONTHLY -> stringResource(R.string.msg_trend_vs_last_month, resultMessage)
        else -> ""
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(14.dp)
            )
        }
        Text(
            text = trendMessage,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp
            ),
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}
