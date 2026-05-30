package dev.muffar.moneyfikasi.recurring_transaction.list.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.icon.BoxedIcon
import dev.muffar.moneyfikasi.common_ui.component.transaction.item.ItemWalletCard
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.domain.model.RecurringEndType
import dev.muffar.moneyfikasi.domain.model.RecurringTransaction
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand
import dev.muffar.moneyfikasi.utils.extensions.LongExt.formattedDate
import dev.muffar.moneyfikasi.utils.extensions.StringExt.capitalize
import java.util.UUID

@Composable
fun RecurringTransactionItem(
    modifier: Modifier = Modifier,
    recurringTransaction: RecurringTransaction,
    onClick: (UUID) -> Unit,
    onToggleActive: (RecurringTransaction) -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick(recurringTransaction.id) },
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    BoxedIcon(
                        icon = recurringTransaction.category?.icon,
                        color = recurringTransaction.category?.color
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = recurringTransaction.name,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        ItemWalletCard(recurringTransaction.wallet ?: Wallet())
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    val amountColor = if (recurringTransaction.type == TransactionType.INCOME) {
                        MoneyfikasiTheme.financeColors.income
                    } else {
                        MoneyfikasiTheme.financeColors.expense
                    }
                    val amountPrefix =
                        if (recurringTransaction.type == TransactionType.INCOME) "+" else "-"

                    Text(
                        text = "$amountPrefix ${recurringTransaction.amount.formatThousand()}",
                        style = MaterialTheme.typography.titleMedium,
                        color = amountColor
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    StatusTag(
                        isActive = recurringTransaction.isActive,
                        isEnded = recurringTransaction.isEnded
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            CommonHorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Rounded.Repeat,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = recurringTransaction.frequency.name.lowercase().capitalize(),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        val endStr = when (recurringTransaction.endType) {
                            RecurringEndType.NEVER -> stringResource(R.string.label_never)
                            RecurringEndType.ON_DATE -> recurringTransaction.endDate?.formattedDate()
                                ?: "-"

                            RecurringEndType.AFTER_OCCURRENCES -> stringResource(
                                R.string.msg_qty_transactions,
                                recurringTransaction.occurrenceCount ?: 0
                            )
                        }
                        Text(
                            text = "${stringResource(R.string.label_ends)}: $endStr",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (!recurringTransaction.isEnded) {
                        Text(
                            text = "${stringResource(R.string.label_next)}: ${
                                recurringTransaction.nextRun?.formattedDate() ?: "-"
                            }",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (recurringTransaction.endType == RecurringEndType.AFTER_OCCURRENCES && !recurringTransaction.isEnded) {
                        val remaining =
                            (recurringTransaction.occurrenceCount
                                ?: 0) - recurringTransaction.executedCount
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.msg_remaining, remaining),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (!recurringTransaction.isEnded) {
                    Switch(
                        checked = recurringTransaction.isActive,
                        onCheckedChange = { onToggleActive(recurringTransaction) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                            checkedTrackColor = MaterialTheme.colorScheme.primary,
                            uncheckedThumbColor = MaterialTheme.colorScheme.surface,
                            uncheckedTrackColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )
                }
            }
        }
    }
}
