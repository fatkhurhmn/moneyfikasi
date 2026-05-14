package dev.muffar.moneyfikasi.common_ui.component.transaction.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.Transaction
import java.util.UUID

@Composable
fun TransactionItem(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    onClick: (UUID) -> Unit,
    showDate: Boolean = false,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(transaction.id) }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TransactionInfo(
            transaction = transaction,
            showDate = showDate,
        )

        TransactionAmount(
            transaction = transaction,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        )
    }
}
