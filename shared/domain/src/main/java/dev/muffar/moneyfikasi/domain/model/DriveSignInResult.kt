package dev.muffar.moneyfikasi.domain.model

import android.app.PendingIntent

sealed interface DriveSignInResult {
    data object Success : DriveSignInResult
    data class NeedsAuthorization(val pendingIntent: PendingIntent?) : DriveSignInResult
    data class Failure(val message: String? = null) : DriveSignInResult
}
