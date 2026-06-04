package dev.muffar.moneyfikasi

import androidx.appcompat.app.AppCompatDelegate
import dev.muffar.moneyfikasi.domain.model.AppTheme

fun AppTheme.toAppCompatNightMode(): Int {
    return when (this) {
        AppTheme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
        AppTheme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
        AppTheme.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }
}
