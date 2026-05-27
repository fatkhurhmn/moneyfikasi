package dev.muffar.moneyfikasi.recurring_transaction.list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.resource.R

@Composable
fun StatusTag(isActive: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = if (isActive) MoneyfikasiTheme.financeColors.incomeContainer else MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.extraSmall
            )
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(
                    color = if (isActive) MoneyfikasiTheme.financeColors.income else MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = CircleShape
                )
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = if (isActive) stringResource(R.string.active) else stringResource(R.string.inactive),
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
            color = if (isActive) MoneyfikasiTheme.financeColors.income else MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}