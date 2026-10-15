package dev.muffar.moneyfikasi.debt.list.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand
import dev.muffar.moneyfikasi.utils.extensions.LongExt.formattedDate

@Composable
fun DebtItem(
    modifier: Modifier = Modifier,
    debt: Debt,
    onClick: (() -> Unit)? = null,
) {
    val progress = if (debt.totalAmount > 0) (debt.paidAmount / debt.totalAmount).toFloat() else 0f
    val financeColors = MoneyfikasiTheme.financeColors
    val color = if (debt.remainingAmount <= 0) financeColors.income else financeColors.info
    val dueDate = debt.dueDate

    PrimaryCard(
        onClick = onClick,
    ) {
        Column(
            modifier = modifier.padding(vertical = 12.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = debt.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 15.sp
                )
                Text(
                    text = debt.totalAmount.formatThousand(),
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
                    text = stringResource(R.string.msg_paid, debt.paidAmount.formatThousand()),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = stringResource(R.string.msg_left, debt.remainingAmount.formatThousand()),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (dueDate != null) {
                Text(
                    text = "${stringResource(R.string.label_due_date)}: ${dueDate.formattedDate()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
