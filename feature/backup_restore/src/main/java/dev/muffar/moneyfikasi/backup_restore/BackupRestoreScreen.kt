package dev.muffar.moneyfikasi.backup_restore

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonAlertDialog
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun BackupRestoreScreen(
    modifier: Modifier = Modifier,
    state: BackupRestoreState,
    eventFlow: SharedFlow<BackupRestoreViewModel.UiEvent>,
    onBackupClick: () -> Unit,
    onRestoreClick: () -> Unit,
    onBackClick: () -> Unit,
    onShowBackupAlert: (Boolean) -> Unit,
    onShowRestoreAlert: (Boolean) -> Unit,
) {

    val context = LocalContext.current
    LaunchedEffect(eventFlow) {
        eventFlow.collectLatest {
            when (it) {
                is BackupRestoreViewModel.UiEvent.ShowMessage -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
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

            val buttonModifier = Modifier
                .height(45.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)

            Button(
                onClick = { onShowBackupAlert(true) },
                modifier = buttonModifier
            ) {
                Text(text = stringResource(R.string.backup))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onShowRestoreAlert(true) },
                modifier = buttonModifier
            ) {
                Text(text = stringResource(R.string.restore))
            }
        }
    }

    if (state.showBackupAlert) {
        CommonAlertDialog(
            title = stringResource(R.string.backup_data),
            message = stringResource(R.string.backup_message),
            positiveText = stringResource(R.string.backup),
            negativeText = stringResource(R.string.cancel),
            onDismiss = { onShowBackupAlert(false) },
            onConfirm = onBackupClick
        )
    }

    if (state.showRestoreAlert) {
        CommonAlertDialog(
            title = stringResource(R.string.restore_data),
            message = stringResource(R.string.restore_message),
            positiveText = stringResource(R.string.restore),
            negativeText = stringResource(R.string.cancel),
            onDismiss = { onShowRestoreAlert(false) },
            onConfirm = onRestoreClick
        )
    }
}