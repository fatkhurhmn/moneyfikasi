package dev.muffar.moneyfikasi.common_ui.component.budget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.icon.BoxedIcon
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.domain.model.Budget
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand

@Composable
fun BudgetItem(
    modifier: Modifier = Modifier,
    budget: Budget,
    spentAmount: Double,
    showCard: Boolean = true,
    onClick: (() -> Unit)? = null,
) {
    val progress = if (budget.amount > 0) (spentAmount / budget.amount).toFloat() else 0f
    val financeColors = MoneyfikasiTheme.financeColors
    val color = when (progress) {
        in 0f..0.7f -> financeColors.info
        in 0.7f..0.90f -> financeColors.warning
        else -> MaterialTheme.colorScheme.error
    }
    val remainingAmount = (budget.amount - spentAmount).let {
        if (it < 0) 0.0 else it
    }

    if (showCard) {
        PrimaryCard(
            onClick = onClick,
        ) {
            BudgetItemContent(
                modifier = modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                budget = budget,
                spentAmount = spentAmount,
                progress = progress,
                remainingAmount = remainingAmount,
                color = color
            )
        }
    } else {
        BudgetItemContent(
            modifier = modifier.padding(vertical = 12.dp, horizontal = 16.dp),
            budget = budget,
            spentAmount = spentAmount,
            progress = progress,
            remainingAmount = remainingAmount,
            color = color
        )
    }
}

@Composable
private fun BudgetItemContent(
    modifier: Modifier = Modifier,
    budget: Budget,
    spentAmount: Double,
    progress: Float,
    remainingAmount: Double,
    color: Color
) {
    Row(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BoxedIcon(
            icon = budget.category.icon,
            color = budget.category.color,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = budget.category.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 15.sp
                )
                Text(
                    text = budget.amount.formatThousand(),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 15.sp
                )
            }
            LinearProgressIndicator(
                progress = { progress.coerceAtMost(1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                strokeCap = StrokeCap.Round,
                gapSize = (-15).dp,
                drawStopIndicator = {},
                color = color
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.msg_spent, spentAmount.formatThousand()),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = stringResource(R.string.msg_left, remainingAmount.formatThousand()),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
