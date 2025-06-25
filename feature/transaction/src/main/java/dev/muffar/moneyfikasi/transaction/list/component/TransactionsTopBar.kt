package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import dev.muffar.moneyfikasi.utils.formatThousand

@Composable
fun TransactionsTopBar(
    modifier: Modifier = Modifier,
    totalBalance: Double,
    isBalanceVisible: Boolean,
    isFilterApplied: Boolean,
    onVisibilityClick: () -> Unit,
    onFilterClick: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(0.75f)
                .padding(end = 32.dp)
        ) {
            Text(
                text = stringResource(R.string.total_balance),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline.copy(0.5f)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val balanceValue = if (isBalanceVisible) {
                    totalBalance.toLong().formatThousand()
                } else {
                    stringResource(R.string.invisible_balance)
                }

                Text(
                    text = balanceValue,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                val visibilityIcon = if (isBalanceVisible) {
                    R.drawable.ic_visibility_on
                } else {
                    R.drawable.ic_visibility_off
                }
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

        Box {
            IconButton(onClick = onFilterClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_filter),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }

            if (isFilterApplied) {
                Icon(
                    imageVector = Icons.Rounded.Circle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .size(10.dp)
                        .align(Alignment.TopStart)
                        .offset(
                            x = 10.dp,
                            y = 5.dp
                        )
                )
            }
        }
    }
}