package dev.muffar.moneyfikasi.common_ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.utils.format
import dev.muffar.moneyfikasi.utils.formatThousand
import org.threeten.bp.LocalDateTime

@Composable
fun GroupTransactionHeader(
    date: LocalDateTime,
    transactions: List<Transaction>,
) {
    val day = date.format("dd")
    val dayOfWeek = date.format("EEE")
    val monthYear = date.format("MMM yyyy")

    val balance = transactions.sumOf {
        if (it.type == TransactionType.INCOME) it.amount
        else -it.amount
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = day,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = dayOfWeek,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = monthYear,
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Text(
            text = balance.toLong().formatThousand(),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Medium,
            )
        )
    }
}