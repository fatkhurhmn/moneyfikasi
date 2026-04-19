package dev.muffar.moneyfikasi.common_ui.component.budget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.transaction_item.ItemCategoryIcon
import dev.muffar.moneyfikasi.common_ui.theme.color.MainColor
import dev.muffar.moneyfikasi.domain.model.Budget
import dev.muffar.moneyfikasi.utils.extensions.formatThousand

@Composable
fun BudgetItem(
    modifier: Modifier = Modifier,
    budget: Budget,
    spentAmount: Double,
    onClick: () -> Unit,
) {
    val progress = if (budget.amount > 0) (spentAmount / budget.amount).toFloat() else 0f
    val color = when(progress){
        in 0f..0.75f -> MainColor.Blue.kindaLight
        in 0.75f..0.90f -> MainColor.Yellow.kindaDark
        else -> MainColor.Red.kindaLight
    }
    val remainingAmount = (budget.amount - spentAmount).let {
        if (it < 0) 0.0 else it
    }

    Row(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .height(70.dp)
            .then(modifier)
    ) {
        ItemCategoryIcon(
            category = budget.category,
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
                    text = spentAmount.formatThousand() + " spent",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = remainingAmount.formatThousand() + " left",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
