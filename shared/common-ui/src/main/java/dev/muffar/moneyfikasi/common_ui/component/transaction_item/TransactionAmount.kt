package dev.muffar.moneyfikasi.common_ui.component.transaction_item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.common_ui.theme.color.FinanceColors
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand

@Composable
fun TransactionAmount(
    transaction: Transaction,
    modifier: Modifier = Modifier
) {
    val financeColors = MoneyfikasiTheme.financeColors

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = getFormattedAmount(transaction.amount, transaction.type),
            style = MaterialTheme.typography.bodyMedium,
            color = getAmountColor(transaction.type, financeColors),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(2.dp))

        ItemWalletCard(transaction.wallet)
    }
}

private fun getFormattedAmount(amount: Double, type: TransactionType): String {
    return when (type) {
        TransactionType.INCOME, TransactionType.TRANSFER_IN -> "+${amount.formatThousand()}"
        TransactionType.EXPENSE, TransactionType.TRANSFER_OUT -> "-${amount.formatThousand()}"
    }
}

private fun getAmountColor(type: TransactionType, financeColors: FinanceColors): Color {
    return when (type) {
        TransactionType.INCOME, TransactionType.TRANSFER_IN -> financeColors.income
        TransactionType.EXPENSE, TransactionType.TRANSFER_OUT -> financeColors.expense
    }
}