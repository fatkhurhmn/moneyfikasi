package dev.muffar.moneyfikasi.transaction.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.formattedDateTime
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.formattedTime

@Composable
fun SubDebtDetailCard(
    transaction: Transaction,
    debtType: DebtType,
    parentName: String
) {
    val titleRes = if (debtType == DebtType.DEBT) {
        if (transaction.type == TransactionType.INCOME) {
            R.string.label_add_debt
        } else {
            R.string.label_pay_debt
        }
    } else {
        if (transaction.type == TransactionType.INCOME) {
            R.string.label_collect_debt
        } else {
            R.string.label_add_receivable
        }
    }
    val parentLabelRes = if (debtType == DebtType.DEBT) {
        R.string.label_from
    } else {
        R.string.label_to
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
        amount = transaction.amount,
        type = transaction.type
    )
    TransactionDetailDivider()
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        RowDetailBody(
            label = stringResource(R.string.label_date),
            value = transaction.date.formattedDateTime()
        )
        RowDetailBody(
            label = stringResource(R.string.label_time),
            value = transaction.date.formattedTime()
        )
        RowDetailBody(
            label = stringResource(parentLabelRes),
            value = parentName
        )
        RowDetailBodyWithIcon(
            label = stringResource(R.string.label_wallet),
            icon = transaction.wallet.icon,
            value = transaction.wallet.name,
            color = transaction.wallet.color
        )
    }
}
