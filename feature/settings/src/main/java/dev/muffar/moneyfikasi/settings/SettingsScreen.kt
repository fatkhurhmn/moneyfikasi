package dev.muffar.moneyfikasi.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.item.SettingItem
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.settings.component.AppearanceSection
import dev.muffar.moneyfikasi.settings.component.DataSection
import dev.muffar.moneyfikasi.settings.component.SecuritySection

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    state: SettingsState,
    onEvent: (SettingsEvent) -> Unit,
    onNotificationClick: () -> Unit,
    onBackupRestoreClick: () -> Unit,
    onExportClick: () -> Unit,
    onAppLockClick: () -> Unit,
    onBackClick: () -> Unit,
) {
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
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
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

            Column {
                Text(
                    text = stringResource(R.string.label_notification_section),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                PrimaryCard {
                    SettingItem(
                        title = stringResource(R.string.label_notification_section),
                        subtitle = stringResource(R.string.msg_notification_description),
                        icon = Icons.Rounded.Notifications,
                        onClick = onNotificationClick
                    )
                }
            }

            SecuritySection(
                onAppLockClick = onAppLockClick
            )
        }
    }
}
