package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.formatThousand

@Composable
fun TotalBalance(
    modifier: Modifier = Modifier,
    totalBalance: Double,
    isBalanceVisible: Boolean,
    onVisibilityClick: () -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.total_balance),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            val balanceValue =
                if (isBalanceVisible) totalBalance.toLong().formatThousand()
                else stringResource(R.string.invisible_balance)

            Text(
                text = balanceValue,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            val visibilityIcon =
                if (isBalanceVisible) R.drawable.ic_visibility_on
                else R.drawable.ic_visibility_off

            Icon(
                painter = painterResource(visibilityIcon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(20.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onVisibilityClick
                    )
            )
        }
    }
}