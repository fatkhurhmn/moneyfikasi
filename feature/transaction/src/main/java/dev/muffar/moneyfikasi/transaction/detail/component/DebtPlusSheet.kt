package dev.muffar.moneyfikasi.transaction.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonButton
import dev.muffar.moneyfikasi.common_ui.component.tabs.DebtTab
import dev.muffar.moneyfikasi.common_ui.component.tabs.DebtTypeTabHeader
import dev.muffar.moneyfikasi.common_ui.component.text_input.BasicAmountInput
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtPlusSheet(
    debtType: DebtType,
    debtName: String,
    remainingAmount: Double,
    selectedTab: Int,
    amount: String,
    error: ErrorMessage,
    isPaymentTab: Boolean,
    onTabSelected: (Int) -> Unit,
    onAmountChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val financeColors = MoneyfikasiTheme.financeColors

    val tabs = if (debtType == DebtType.DEBT) {
        listOf(
            DebtTab(R.string.label_add_debt, financeColors.income),
            DebtTab(R.string.label_pay_debt, financeColors.expense)
        )
    } else {
        listOf(
            DebtTab(R.string.label_collect_debt, financeColors.income),
            DebtTab(R.string.label_add_receivable, financeColors.expense)
        )
    }
    val confirmRes = if (isPaymentTab) {
        if (debtType == DebtType.DEBT) R.string.label_pay_debt else R.string.label_collect_debt
    } else {
        R.string.action_save
    }

    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        sheetGesturesEnabled = false
    ) {
        BottomSheetTitle(debtName)
        Spacer(Modifier.height(16.dp))
        DebtTypeTabHeader(
            modifier = Modifier.padding(horizontal = 16.dp),
            tabs = tabs,
            selectedTab = selectedTab,
            onTabSelected = onTabSelected
        )
        Spacer(Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (isPaymentTab) {
                Text(
                    text = stringResource(R.string.msg_left, remainingAmount.formatThousand()),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            BasicAmountInput(
                amount = amount,
                error = error,
                onAmountChange = onAmountChange
            )
            CommonButton(
                text = stringResource(confirmRes),
                modifier = Modifier.fillMaxWidth(),
                onClick = onConfirm
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}
