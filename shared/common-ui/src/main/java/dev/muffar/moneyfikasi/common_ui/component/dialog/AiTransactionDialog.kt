package dev.muffar.moneyfikasi.common_ui.component.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.AiTransactionResult
import dev.muffar.moneyfikasi.resource.R

@Composable
fun AiTransactionDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (String) -> Unit,
    isProcessing: Boolean = false
) {
    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = stringResource(R.string.title_ai_transaction))
        },
        text = {
            Column {
                Text(
                    text = stringResource(R.string.msg_ai_transaction_description),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = {
                        Text(text = stringResource(R.string.label_enter_amount))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isProcessing
                )
                if (isProcessing) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.msg_ai_processing),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(text) },
                enabled = text.isNotBlank() && !isProcessing
            ) {
                Text(text = stringResource(R.string.action_process))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
                enabled = !isProcessing
            ) {
                Text(text = stringResource(R.string.action_cancel))
            }
        }
    )
}
