package dev.muffar.moneyfikasi.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.BottomSheetTitle
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.SwitchPickerOptionItem
import dev.muffar.moneyfikasi.resource.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardSettingsSheet(
    isQuickTransactionVisible: Boolean,
    isBudgetVisible: Boolean,
    onQuickTransactionVisibilityChange: (Boolean) -> Unit,
    onBudgetVisibilityChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        sheetGesturesEnabled = false
    ) {
        BottomSheetTitle(stringResource(R.string.title_dashboard_settings))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.msg_dashboard_settings_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )

            SwitchPickerOptionItem(
                checked = isQuickTransactionVisible,
                icon = Icons.Rounded.Bolt,
                title = stringResource(R.string.title_quick_transactions),
                subtitle = stringResource(R.string.msg_quick_transactions_dashboard_description),
                onCheckedChange = onQuickTransactionVisibilityChange
            )

            SwitchPickerOptionItem(
                checked = isBudgetVisible,
                icon = Icons.Rounded.AccountBalanceWallet,
                title = stringResource(R.string.title_budgets),
                subtitle = stringResource(R.string.msg_budget_dashboard_description),
                onCheckedChange = onBudgetVisibilityChange
            )
        }
    }
}
