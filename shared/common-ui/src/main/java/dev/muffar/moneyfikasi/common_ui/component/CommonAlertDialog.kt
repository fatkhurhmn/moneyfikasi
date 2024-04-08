package dev.muffar.moneyfikasi.common_ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CommonAlertDialog(
    modifier: Modifier = Modifier,
    title: String,
    message: String,
    positiveText: String,
    negativeText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = positiveText, color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = negativeText)
            }
        },
        title = { Text(text = title, style = MaterialTheme.typography.titleMedium) },
        text = { Text(text = message) },
    )
}