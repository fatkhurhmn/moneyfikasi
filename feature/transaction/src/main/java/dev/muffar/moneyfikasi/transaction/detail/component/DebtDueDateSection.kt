package dev.muffar.moneyfikasi.transaction.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.LongExt.formattedDate
import dev.muffar.moneyfikasi.utils.extensions.LongExt.toLocalDateTime
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit

@Composable
fun DebtDueDateSection(
    modifier: Modifier = Modifier,
    dueDate: Long,
    remainingAmount: Double
) {
    val financeColors = MoneyfikasiTheme.financeColors
    val daysLeft = remember(dueDate) {
        ChronoUnit.DAYS.between(
            LocalDate.now(),
            dueDate.toLocalDateTime().toLocalDate()
        ).toInt()
    }
    val statusText: String
    val statusColor = when {
        remainingAmount <= 0 -> {
            statusText = stringResource(R.string.label_paid)
            financeColors.income
        }
        daysLeft > 0 -> {
            statusText = stringResource(R.string.msg_due_in, daysLeft)
            MaterialTheme.colorScheme.onSurfaceVariant
        }
        daysLeft == 0 -> {
            statusText = stringResource(R.string.msg_due_today)
            financeColors.expense
        }
        else -> {
            statusText = stringResource(R.string.msg_overdue, -daysLeft)
            financeColors.expense
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.label_due_date),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = dueDate.formattedDate(),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = statusText,
                style = MaterialTheme.typography.bodySmall,
                color = statusColor
            )
        }
    }
}
