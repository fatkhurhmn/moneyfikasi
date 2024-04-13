package dev.muffar.moneyfikasi.transaction.detail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowCircleDown
import androidx.compose.material.icons.twotone.ArrowCircleUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.theme.color.MainColor
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.resource.R

@Composable
fun TransactionDetailHeader(
    modifier: Modifier = Modifier,
    amount: String,
    type: TransactionType,
) {
    val mAmount = if (type == TransactionType.INCOME) {
        "+ $amount"
    } else {
        "- $amount"
    }

    val color = if (type == TransactionType.INCOME) {
        MainColor.Green.primary
    } else {
        MainColor.Red.primary
    }

    val icon = if (type == TransactionType.INCOME) {
        Icons.TwoTone.ArrowCircleDown
    } else {
        Icons.TwoTone.ArrowCircleUp
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color.copy(alpha = 0.7f),
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.transaction_summary),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outlineVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = mAmount,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = color
        )
    }
}