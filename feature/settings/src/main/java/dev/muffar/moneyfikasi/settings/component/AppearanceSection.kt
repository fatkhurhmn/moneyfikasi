package dev.muffar.moneyfikasi.settings.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.ThemePickerSheet
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.item.SettingItem
import dev.muffar.moneyfikasi.domain.model.AppTheme
import dev.muffar.moneyfikasi.resource.R

@Composable
fun AppearanceSection(
    appTheme: AppTheme,
    onAppThemeChanged: (AppTheme) -> Unit,
) {
    var showThemePicker by remember { mutableStateOf(false) }

    val themeSubtitle = when (appTheme) {
        AppTheme.LIGHT -> stringResource(R.string.label_light)
        AppTheme.DARK -> stringResource(R.string.label_dark)
        AppTheme.SYSTEM -> stringResource(R.string.label_system_default)
    }

    Column {
        Text(
            text = stringResource(R.string.label_appearance_section),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        PrimaryCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            SettingItem(
                title = stringResource(R.string.label_theme),
                subtitle = themeSubtitle,
                icon = Icons.Rounded.Palette,
                onClick = { showThemePicker = true }
            )
        }

        AnimatedVisibility(showThemePicker) {
            ThemePickerSheet(
                selectedTheme = appTheme,
                onThemeSelect = onAppThemeChanged,
                onDismissRequest = { showThemePicker = false }
            )
        }
    }
}
