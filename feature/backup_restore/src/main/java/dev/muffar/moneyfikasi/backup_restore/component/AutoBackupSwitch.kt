package dev.muffar.moneyfikasi.backup_restore.component

import android.net.Uri
import android.os.Environment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Update
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.item.SettingSwitchItem
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
            val docs =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            val defaultFolder = File(docs, "Moneyfikasi")
            if (!defaultFolder.exists()) {
                defaultFolder.mkdirs()
            }
            onFolderSelected(Uri.fromFile(defaultFolder))
        }
    }

    SettingSwitchItem(
        isEnabled = isEnabled,
        onEnabledChange = onEnabledChange,
        title = stringResource(R.string.msg_automatic_backup),
        subtitle = stringResource(R.string.msg_auto_backup_description),
        icon = Icons.Rounded.Update
    )
}
