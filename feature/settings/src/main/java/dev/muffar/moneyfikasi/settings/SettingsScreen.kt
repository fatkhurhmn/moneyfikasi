package dev.muffar.moneyfikasi.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.notification.component.NotificationSection
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.settings.component.AppearanceSection
import dev.muffar.moneyfikasi.settings.component.DataSection
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
                state = state.notification,
                onEvent = { notificationEvent ->
                    onEvent(SettingsEvent.Notification(notificationEvent))
                }
            )

            SecuritySection(
                onAppLockClick = onAppLockClick
            )
        }
    }
}
