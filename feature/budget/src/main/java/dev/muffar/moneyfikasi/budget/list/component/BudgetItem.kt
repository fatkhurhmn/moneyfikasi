package dev.muffar.moneyfikasi.budget.list.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.transaction_item.ItemCategoryIcon
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
    val remainingAmount = budget.amount - spentAmount

    Column(
        modifier = modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (budget.category != null) {
                    ItemCategoryIcon(category = budget.category!!)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = budget.category!!.name, style = MaterialTheme.typography.titleMedium)
                }
            }
            Text(
                text = budget.amount.formatThousand(),
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { progress.coerceAtMost(1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            strokeCap = StrokeCap.Round,
            gapSize = (-15).dp,
            drawStopIndicator = {},
            color = if (progress > 1f) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
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
                color = if (remainingAmount < 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
