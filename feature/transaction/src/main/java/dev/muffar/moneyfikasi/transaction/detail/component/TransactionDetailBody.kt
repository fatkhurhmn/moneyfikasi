package dev.muffar.moneyfikasi.transaction.detail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.Wallet
import org.threeten.bp.LocalDateTime

@Composable
fun TransactionDetailBody(
    modifier: Modifier = Modifier,
    date: LocalDateTime?,
    wallet: Wallet?,
    category: Category?,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        TransactionDetailDate(date)
        Spacer(Modifier.height(24.dp))
        TransactionDetailTime(date)
        Spacer(Modifier.height(24.dp))
        TransactionDetailCategory(category)
        Spacer(Modifier.height(24.dp))
        TransactionDetailWallet(wallet)
    }
}