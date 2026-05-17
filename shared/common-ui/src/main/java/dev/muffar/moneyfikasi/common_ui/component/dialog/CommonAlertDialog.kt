package dev.muffar.moneyfikasi.common_ui.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonButton
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonOutlinedButton

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
            CommonButton(
                onClick = onConfirm,
                text = positiveText
            )
        },
        dismissButton = {
            CommonOutlinedButton(
                onClick = onDismiss,
                text = negativeText,
                color = MaterialTheme.colorScheme.error
            )
        },
        title = { Text(text = title, style = MaterialTheme.typography.titleMedium) },
        text = { Text(text = message) },
        containerColor = MaterialTheme.colorScheme.surface
    )
}