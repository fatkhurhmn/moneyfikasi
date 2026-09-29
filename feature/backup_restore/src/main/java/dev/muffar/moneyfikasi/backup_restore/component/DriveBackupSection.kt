package dev.muffar.moneyfikasi.backup_restore.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.backup_restore.BackupRestoreState

@Composable
fun DriveBackupSection(
    state: BackupRestoreState,
    onSignInClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onBackupClick: () -> Unit,
    onRestoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isBusy = state.isDriveLoading || state.isLoading

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DriveBackupButtonsCard(
            isSignedIn = state.isDriveSignedIn,
            isBusy = isBusy,
            canRestore = state.driveBackup != null,
            onSignInClick = onSignInClick,
            onBackupClick = onBackupClick,
            onRestoreClick = onRestoreClick
        )

        if (state.isDriveSignedIn) {
            DriveLatestBackupCard(
                modifiedTime = state.driveBackup?.modifiedTime
            )
            DriveAccountCard(
                email = state.driveAccountEmail,
                onSignOutClick = onSignOutClick
            )
        }
    }
}
