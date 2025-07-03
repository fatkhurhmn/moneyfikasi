package dev.muffar.moneyfikasi.backup_restore

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.backup_restore.component.BackupRestoreButton
import dev.muffar.moneyfikasi.backup_restore.component.BackupRestoreNotes
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun BackupRestoreScreen(
    modifier: Modifier = Modifier,
    eventFlow: SharedFlow<BackupRestoreViewModel.UiEvent>,
    onBackupClick: (Uri) -> Unit,
    onRestoreClick: (Uri) -> Unit,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    val dirBackupLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
            if (it != null) {
                onBackupClick(it)
            }
        }

    val dirRestoreLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocumentTree()) {
            if (it != null) {
                onRestoreClick(it)
            }
        }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.backup_restore),
                onBackClick = onBackClick
            )
        }
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .padding(vertical = 4.dp)
        ) {

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_restore_backup2),
                    contentDescription = stringResource(R.string.backup_restore),
                    modifier = Modifier
                        .padding(vertical = 32.dp)
                        .size(150.dp)
                )
            }

            BackupRestoreButton(
                title = stringResource(R.string.backup),
                onClick = {
                    dirBackupLauncher.launch(null)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            BackupRestoreButton(
                title = stringResource(R.string.restore),
                onClick = {
                    dirRestoreLauncher.launch(null)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            BackupRestoreNotes()
        }
    }

    LaunchedEffect(eventFlow) {
        eventFlow.collectLatest {
            when (it) {
                is BackupRestoreViewModel.UiEvent.ShowMessage -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}