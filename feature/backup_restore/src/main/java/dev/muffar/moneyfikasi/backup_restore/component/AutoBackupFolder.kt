package dev.muffar.moneyfikasi.backup_restore.component

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import dev.muffar.moneyfikasi.resource.R

@Composable
fun AutoBackupFolder(
    folderUri: String,
    onFolderSelected: (Uri) -> Unit
) {
    val formattedFolderUri = remember(folderUri) {
        if (folderUri.isEmpty()) {
            ""
        } else {
            val uri = folderUri.toUri()
            val path = uri.path?.let { Uri.decode(it) } ?: folderUri
            val folderPath = path.split(":").lastOrNull()?.trim('/') ?: path
            "sdcard/$folderPath"
        }
    }
    val dirLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
            if (it != null) {
                onFolderSelected(it)
            }
        }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { dirLauncher.launch(null) }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = stringResource(R.string.select_backup_folder),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = formattedFolderUri.ifEmpty { stringResource(R.string.select_backup_folder) },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}