package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.usecase.preferences.backup.BackupSettingsUseCases
import dev.muffar.moneyfikasi.domain.usecase.preferences.security.SecuritySettingsUseCases
import dev.muffar.moneyfikasi.domain.usecase.preferences.ui.UiSettingsUseCases

data class PreferencesUseCases(
    val ui: UiSettingsUseCases,
    val backup: BackupSettingsUseCases,
    val security: SecuritySettingsUseCases
)
