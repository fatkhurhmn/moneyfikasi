package dev.muffar.moneyfikasi.statistic.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand

@Composable
fun TotalAmount(totalAmount: Double, type: TransactionType) {
    val total = totalAmount.formatThousand()
    val financeColors = MoneyfikasiTheme.financeColors

    val color = if (type == TransactionType.INCOME) financeColors.income else financeColors.expense

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.total),
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
        )
        Text(
            text = if (type == TransactionType.INCOME) "+$total" else "-$total",
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
            color = color
        )
    }
}