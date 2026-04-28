package dev.muffar.moneyfikasi.data.preferences

import javax.inject.Inject

class AppPreferences @Inject constructor(
    val ui: UiPreferences,
    val backup: BackupPreferences,
    val security: SecurityPreferences,
)
