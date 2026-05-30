package dev.muffar.moneyfikasi.notification.component

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.item.SettingSwitchItem
import dev.muffar.moneyfikasi.notification.NotificationEvent
import dev.muffar.moneyfikasi.notification.NotificationState
import dev.muffar.moneyfikasi.resource.R

@Composable
fun NotificationSection(
    state: NotificationState,
    onEvent: (NotificationEvent) -> Unit,
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
        onEvent(NotificationEvent.AllowNotificationChanged(isGranted))
    }

    Column {
        Text(
            text = stringResource(R.string.label_notification_section),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        PrimaryCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            SettingSwitchItem(
                title = stringResource(R.string.label_allow_notifications),
                subtitle = stringResource(R.string.msg_allow_notifications_description),
                icon = Icons.Rounded.Notifications,
                isEnabled = state.isAllowNotification,
                onEnabledChange = { isEnabled ->
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
                            onEvent(NotificationEvent.AllowNotificationChanged(true))
                        }
                    } else {
                        if (isGranted) {
                            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                            }
                            context.startActivity(intent)
                        }
                        onEvent(NotificationEvent.AllowNotificationChanged(false))
                    }
                }
            )

            CommonHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

            SettingSwitchItem(
                title = stringResource(R.string.label_recurring_transaction_notifications),
                subtitle = stringResource(R.string.msg_recurring_transaction_notifications_description),
                icon = Icons.Rounded.Notifications,
                isEnabled = state.isRecurringTransactionNotificationEnabled,
                enabled = state.isAllowNotification,
                onEnabledChange = { isEnabled ->
                    onEvent(NotificationEvent.RecurringTransactionNotificationChanged(isEnabled))
                }
            )
        }
    }
}
