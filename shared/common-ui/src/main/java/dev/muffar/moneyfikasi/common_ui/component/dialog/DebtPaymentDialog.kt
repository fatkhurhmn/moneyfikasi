package dev.muffar.moneyfikasi.common_ui.component.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonButton
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonOutlinedButton
import dev.muffar.moneyfikasi.common_ui.component.text_input.BasicAmountInput
import dev.muffar.moneyfikasi.domain.model.DebtType
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand

@Composable
fun DebtPaymentDialog(
    debtName: String,
    debtType: DebtType,
    remainingAmount: Double,
    amount: String,
    error: ErrorMessage,
    onAmountChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    val titleRes = if (debtType == DebtType.DEBT) {
        R.string.label_pay_debt
    } else {
        R.string.label_collect_debt
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            CommonButton(
                text = stringResource(R.string.action_save),
                onClick = onConfirm
            )
        },
        dismissButton = {
            CommonOutlinedButton(
                text = stringResource(R.string.action_cancel),
                color = MaterialTheme.colorScheme.error,
                onClick = onDismiss
            )
        },
        title = { Text(text = stringResource(titleRes), style = MaterialTheme.typography.titleMedium) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = debtName,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.msg_left, remainingAmount.formatThousand()),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(12.dp))
                BasicAmountInput(
                    amount = amount,
                    error = error,
                    onAmountChange = onAmountChange
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    )
}
