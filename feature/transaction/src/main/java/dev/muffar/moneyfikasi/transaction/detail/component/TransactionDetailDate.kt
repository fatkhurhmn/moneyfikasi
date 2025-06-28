package dev.muffar.moneyfikasi.transaction.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.format
import org.threeten.bp.LocalDateTime

@Composable
fun TransactionDetailDate(
    date: LocalDateTime?,
) {
    val formattedDate = date?.format("dd MMM, yyyy") ?: ""
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.date),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 18.sp
                )
            )
        }
    }
}