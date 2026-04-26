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
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.format
import org.threeten.bp.LocalDateTime

@Composable
fun GroupTransactionHeaderV2(
    date: LocalDateTime,
    balanceOnDate: Double
) {
    val day = date.format("dd")
    val dayOfWeek = date.format("EEE")
    val monthYear = date.format("MMM yyyy")

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
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Text(
            text = balanceOnDate.formatThousand(),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Medium,
            )
        )
    }
}