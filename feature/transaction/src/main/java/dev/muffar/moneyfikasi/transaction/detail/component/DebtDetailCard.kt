package dev.muffar.moneyfikasi.transaction.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.domain.model.Debt
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand
import dev.muffar.moneyfikasi.utils.extensions.LongExt.formattedDate

@Composable
fun DebtDetailCard(
    debt: Debt,
    wallet: Wallet
) {
    val financeColors = MoneyfikasiTheme.financeColors
    val progress = if (debt.totalAmount > 0) {
        (debt.paidAmount / debt.totalAmount).toFloat()
    } else {
        0f
    }
    val barColor = if (debt.remainingAmount <= 0) financeColors.income else financeColors.info
    val counterpartyLabelRes = if (debt.type == DebtType.DEBT) {
        R.string.label_from
    } else {
        R.string.label_to
    }
    val titleRes = if (debt.type == DebtType.DEBT) {
        R.string.label_hutang
    } else {
        R.string.label_piutang
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(titleRes),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
    Spacer(modifier = Modifier.height(32.dp))
    TransactionDetailAmount(
        amount = debt.totalAmount,
        type = debt.type.toTransactionType()
    )
    TransactionDetailDivider()
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        RowDetailBody(
            label = stringResource(counterpartyLabelRes),
            value = debt.name
        )
        RowDetailBody(
            label = stringResource(R.string.label_debt_date),
            value = debt.createdAt.formattedDate()
        )
        debt.dueDate?.let { dueDate ->
            RowDetailBody(
                label = stringResource(R.string.label_due_date),
                value = dueDate.formattedDate()
            )
        }
        RowDetailBodyWithIcon(
            label = stringResource(R.string.label_wallet),
            icon = wallet.icon,
            value = wallet.name,
            color = wallet.color
        )
    }
    TransactionDetailDivider()
    LinearProgressIndicator(
        progress = { progress.coerceAtMost(1f) },
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp),
        trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
        strokeCap = StrokeCap.Round,
        gapSize = (-15).dp,
        drawStopIndicator = {},
        color = barColor
    )
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.label_paid),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = stringResource(R.string.label_total),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = debt.paidAmount.formatThousand(),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = debt.totalAmount.formatThousand(),
            style = MaterialTheme.typography.titleMedium
        )
    }
}
