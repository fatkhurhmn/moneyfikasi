package dev.muffar.moneyfikasi.settings

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.settings.component.AppearanceSection
import dev.muffar.moneyfikasi.settings.component.DataSection
import dev.muffar.moneyfikasi.settings.component.NotificationSection
import dev.muffar.moneyfikasi.settings.component.SecuritySection

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    state: SettingsState,
    onEvent: (SettingsEvent) -> Unit,
    onBackupRestoreClick: () -> Unit,
    onExportClick: () -> Unit,
    onAppLockClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted && activity != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            )
            if (!showRationale) {
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                }
                context.startActivity(intent)
            }
        }
        onEvent(SettingsEvent.AllowNotificationChanged(isGranted))
    }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.menu_settings),
                onBackClick = onBackClick
            )
        }
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AppearanceSection(
                appTheme = state.appTheme,
                appLanguage = state.appLanguage,
                onAppThemeChanged = { theme ->
                    onEvent(SettingsEvent.AppThemeChanged(theme))
                },
                onAppLanguageChanged = { language ->
                    onEvent(SettingsEvent.AppLanguageChanged(language))
                }
            )

            DataSection(
                onBackupRestoreClick = onBackupRestoreClick,
                onExportClick = onExportClick
            )

            NotificationSection(
                isAllowNotification = state.isAllowNotification,
                isRecurringTransactionNotificationEnabled =
                    state.isRecurringTransactionNotificationEnabled,
                onAllowNotificationChanged = { isEnabled ->
                    val isGranted = NotificationManagerCompat.from(context).areNotificationsEnabled()
                    if (isEnabled) {
                        if (!isGranted) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                                }
                                context.startActivity(intent)
                            }
                        } else {
                            onEvent(SettingsEvent.AllowNotificationChanged(true))
                        }
                    } else {
                        if (isGranted) {
                            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                            }
                            context.startActivity(intent)
                        }
                        onEvent(SettingsEvent.AllowNotificationChanged(false))
                    }
                },
                onRecurringTransactionNotificationChanged = { isEnabled ->
                    onEvent(SettingsEvent.RecurringTransactionNotificationChanged(isEnabled))
                }
            )

            SecuritySection(
                onAppLockClick = onAppLockClick
            )
        }
    }
}
