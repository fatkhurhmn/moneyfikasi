package dev.muffar.moneyfikasi.settings.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.item.SettingItem
import dev.muffar.moneyfikasi.resource.R

@Composable
fun NotificationSection(
    onNotificationClick: () -> Unit,
) {
    PrimaryCard {
        SettingItem(
            title = stringResource(R.string.label_notification_section),
            subtitle = stringResource(R.string.msg_notification_description),
            icon = Icons.Rounded.Notifications,
            onClick = onNotificationClick
        )
    }
}

