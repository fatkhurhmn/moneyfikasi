package dev.muffar.moneyfikasi.debt.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonAddButton
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.icon.BoxedIcon
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.debt.menu.component.DebtMenuAddSheet
import dev.muffar.moneyfikasi.domain.model.AppIcon
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand

@Composable
fun DebtMenuScreen(
    modifier: Modifier = Modifier,
    state: DebtMenuState,
    onDebtClick: (DebtType) -> Unit,
    onAddDebtClick: (DebtType) -> Unit,
    onBackClick: () -> Unit
) {
    val financeColors = MoneyfikasiTheme.financeColors
    var showAddSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.title_debts),
                onBackClick = onBackClick
            )
        },
        floatingActionButton = {
            CommonAddButton(
                onClick = { showAddSheet = true }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DebtMenuCard(
                icon = AppIcon.Payments.name,
                iconColor = financeColors.income.toArgb().toLong(),
                title = stringResource(R.string.label_hutang),
                subtitle = stringResource(R.string.label_me),
                count = state.unpaidDebts.size,
                remainingAmount = state.unpaidDebtRemaining,
                onClick = { onDebtClick(DebtType.DEBT) }
            )
            DebtMenuCard(
                icon = AppIcon.ReceiptLong.name,
                iconColor = financeColors.expense.toArgb().toLong(),
                title = stringResource(R.string.label_piutang),
                subtitle = stringResource(R.string.label_to_others),
                count = state.unpaidReceivables.size,
                remainingAmount = state.unpaidReceivableRemaining,
                onClick = { onDebtClick(DebtType.RECEIVABLE) }
            )
        }
    }

    if (showAddSheet) {
        DebtMenuAddSheet(
            onAddDebt = onAddDebtClick,
            onDismissRequest = { showAddSheet = false }
        )
    }
}

@Composable
private fun DebtMenuCard(
    modifier: Modifier = Modifier,
    icon: String,
    iconColor: Long,
    title: String,
    subtitle: String,
    count: Int,
    remainingAmount: Double,
    onClick: () -> Unit
) {
    PrimaryCard(onClick = onClick) {
        Column(
            modifier = modifier.padding(vertical = 12.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                BoxedIcon(
                    icon = icon,
                    color = iconColor
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            CommonHorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.8f)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.label_sum),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = stringResource(R.string.msg_transaction_count, count),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(R.string.label_unpaid),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = remainingAmount.formatThousand(),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
