package dev.muffar.moneyfikasi.common_ui.component.text_input

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.resource.R

@Composable
fun TextInputDecoration(
    isFocus: Boolean,
    label: String,
    error: ErrorMessage,
    enabled: Boolean,
    isEmpty: Boolean,
    leadingIcon: @Composable (() -> Unit)?,
    onClear: (() -> Unit)?,
    innerTextField: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .border(
                width = 1.dp,
                color = when {
                    error.message != null || error.resId != null -> MaterialTheme.colorScheme.error
                    isFocus -> MaterialTheme.colorScheme.primary
                    else -> if (!enabled) MaterialTheme.colorScheme.outline else Color.Transparent
                },
                shape = MaterialTheme.shapes.medium
            )
            .background(
                color = if (enabled) {
                    MaterialTheme.colorScheme.secondaryContainer.copy(0.5f)
                } else {
                    MaterialTheme.colorScheme.background
                },
                shape = MaterialTheme.shapes.medium
            )
            .padding(
                horizontal = 12.dp,
                vertical = 8.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            Box(
                modifier = Modifier.padding(end = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                leadingIcon()
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            val isLabelFloating = !isEmpty || isFocus

            if (isLabelFloating) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }

            Box(contentAlignment = Alignment.CenterStart) {
                if (isEmpty && !isFocus) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }
                innerTextField()
            }
        }

        if (onClear != null && !isEmpty) {
            Icon(
                imageVector = Icons.Rounded.Clear,
                contentDescription = stringResource(R.string.action_clear),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(16.dp)
                    .clickable { onClear() }
            )
        }
    }
}
