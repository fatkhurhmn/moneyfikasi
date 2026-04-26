package dev.muffar.moneyfikasi.backup_restore.component

import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.resource.R
import java.io.File

@Composable
fun AutoBackupSwitch(
    isEnabled: Boolean,
    folderUri: String,
    onEnabledChange: (Boolean) -> Unit,
    onFolderSelected: (Uri) -> Unit,
) {
    LaunchedEffect(isEnabled) {
        if (isEnabled && folderUri.isEmpty()) {
            val docs = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            val defaultFolder = File(docs, "Moneyfikasi")
            if (!defaultFolder.exists()) {
                defaultFolder.mkdirs()
            }
            onFolderSelected(Uri.fromFile(defaultFolder))
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEnabledChange(!isEnabled) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.automatic_backup),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stringResource(R.string.auto_backup_description),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Switch(
            checked = isEnabled,
            onCheckedChange = onEnabledChange
        )
    }
}