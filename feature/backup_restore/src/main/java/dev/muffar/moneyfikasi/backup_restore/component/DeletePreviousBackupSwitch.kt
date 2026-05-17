package dev.muffar.moneyfikasi.backup_restore.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonSwitch
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.resource.R

@Composable
fun DeletePreviousBackupSwitch(
    isEnabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
) {
    PrimaryCard(
        onClick = { onEnabledChange(!isEnabled) }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.DeleteOutline,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            CommonSwitch(
                isEnabled = isEnabled,
                onEnabledChange = onEnabledChange,
                title = stringResource(R.string.delete_previous_backup),
                description = stringResource(R.string.delete_previous_backup_description),
                clickableText = false
            )
        }
    }
}
