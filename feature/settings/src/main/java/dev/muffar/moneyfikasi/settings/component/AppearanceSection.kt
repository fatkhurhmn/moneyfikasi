package dev.muffar.moneyfikasi.settings.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.LanguagePickerSheet
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.ThemePickerSheet
import dev.muffar.moneyfikasi.common_ui.component.container.PrimaryCard
import dev.muffar.moneyfikasi.common_ui.component.item.SettingItem
import dev.muffar.moneyfikasi.domain.model.AppLanguage
import dev.muffar.moneyfikasi.domain.model.AppTheme
import dev.muffar.moneyfikasi.resource.R

@Composable
fun AppearanceSection(
    appTheme: AppTheme,
    appLanguage: AppLanguage,
    onAppThemeChanged: (AppTheme) -> Unit,
    onAppLanguageChanged: (AppLanguage) -> Unit,
) {
    var showThemePicker by remember { mutableStateOf(false) }
    var showLanguagePicker by remember { mutableStateOf(false) }

    val themeSubtitle = when (appTheme) {
        AppTheme.LIGHT -> stringResource(R.string.label_light)
        AppTheme.DARK -> stringResource(R.string.label_dark)
        AppTheme.SYSTEM -> stringResource(R.string.label_system_default)
    }

    val languageSubtitle = when (appLanguage) {
        AppLanguage.ENGLISH -> stringResource(R.string.label_english)
        AppLanguage.INDONESIAN -> stringResource(R.string.label_indonesian)
        AppLanguage.SYSTEM -> stringResource(R.string.label_system_default)
    }


    PrimaryCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            SettingItem(
                title = stringResource(R.string.label_theme),
                subtitle = themeSubtitle,
                icon = Icons.Rounded.Palette,
                onClick = { showThemePicker = true }
            )
            CommonHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            SettingItem(
                title = stringResource(R.string.label_language),
                subtitle = languageSubtitle,
                icon = Icons.Rounded.Language,
                onClick = { showLanguagePicker = true }
            )
        }
    }

    AnimatedVisibility(showThemePicker) {
        ThemePickerSheet(
            selectedTheme = appTheme,
            onThemeSelect = onAppThemeChanged,
            onDismissRequest = { showThemePicker = false }
        )
    }

    AnimatedVisibility(showLanguagePicker) {
        LanguagePickerSheet(
            selectedLanguage = appLanguage,
            onLanguageSelect = onAppLanguageChanged,
            onDismissRequest = { showLanguagePicker = false }
        )
    }
}
