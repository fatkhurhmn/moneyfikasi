package dev.muffar.moneyfikasi.domain.usecase.preferences

import dev.muffar.moneyfikasi.domain.usecase.preferences.backup.BackupPreferencesUseCases
import dev.muffar.moneyfikasi.domain.usecase.preferences.security.SecurityPreferencesUseCases
import dev.muffar.moneyfikasi.domain.usecase.preferences.ui.UiPreferencesUseCases

data class AppPreferencesUseCases(
    val ui: UiPreferencesUseCases,
    val backup: BackupPreferencesUseCases,
    val security: SecurityPreferencesUseCases
)
