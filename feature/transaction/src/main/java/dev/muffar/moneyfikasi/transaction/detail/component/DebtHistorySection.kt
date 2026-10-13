package dev.muffar.moneyfikasi.transaction.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.formattedDateTime

@Composable
fun DebtHistorySection(
    modifier: Modifier = Modifier,
    history: List<Transaction>
) {
    if (history.isEmpty()) return

    val financeColors = MoneyfikasiTheme.financeColors

    Column(modifier = modifier.fillMaxWidth()) {
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.label_history),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))
        history.forEach { transaction ->
            val isExpense = transaction.type == TransactionType.EXPENSE
            val color = if (isExpense) financeColors.expense else financeColors.income
            val sign = if (isExpense) "-" else "+"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = transaction.note?.takeIf { it.isNotBlank() }
                            ?: transaction.category.name,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = transaction.date.formattedDateTime(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = sign + transaction.amount.formatThousand(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = color
                )
            }
        }
    }
}
