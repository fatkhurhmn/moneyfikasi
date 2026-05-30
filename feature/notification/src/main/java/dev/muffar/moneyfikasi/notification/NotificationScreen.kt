package dev.muffar.moneyfikasi.notification

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.item.SettingSwitchItem
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.resource.R

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier,
    state: NotificationState,
    onEvent: (NotificationEvent) -> Unit,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val isTiramisu = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    val showRationale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        activity != null && ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            Manifest.permission.POST_NOTIFICATIONS
        )
    } else {
        false
    }

    val settingsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val isGranted = NotificationManagerCompat.from(context).areNotificationsEnabled()
        onEvent(NotificationEvent.AllowNotificationChanged(isGranted))
    }

    val openNotificationSettings = {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        }
        settingsLauncher.launch(intent)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted && isTiramisu && !showRationale) {
            openNotificationSettings()
        }
        onEvent(NotificationEvent.AllowNotificationChanged(isGranted))
    }

    val onAllowNotificationChange: (Boolean) -> Unit = { isEnabled ->
        val isGranted = NotificationManagerCompat.from(context).areNotificationsEnabled()
        when {
            isEnabled && !isGranted && isTiramisu -> permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            isEnabled && !isGranted -> openNotificationSettings()
            !isEnabled && isGranted -> openNotificationSettings()
            else -> onEvent(NotificationEvent.AllowNotificationChanged(isEnabled))
        }
    }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.label_notification_section),
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PrimaryCard(modifier = Modifier.fillMaxWidth()) {
                SettingSwitchItem(
                    title = stringResource(R.string.label_allow_notifications),
                    subtitle = stringResource(R.string.msg_allow_notifications_description),
                    icon = Icons.Rounded.Notifications,
                    isEnabled = state.isAllowNotification,
                    onEnabledChange = onAllowNotificationChange
                )
            }

            AnimatedVisibility(
                visible = state.isAllowNotification,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                PrimaryCard(modifier = Modifier.fillMaxWidth()) {
                    SettingSwitchItem(
                        title = stringResource(R.string.label_recurring_transaction_notifications),
                        subtitle = stringResource(R.string.msg_recurring_transaction_notifications_description),
                        icon = Icons.Rounded.Notifications,
                        isEnabled = state.isRecurringTransactionNotificationEnabled,
                        onEnabledChange = {
                            onEvent(
                                NotificationEvent.RecurringTransactionNotificationChanged(
                                    it
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}
