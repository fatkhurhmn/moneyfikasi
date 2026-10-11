package dev.muffar.moneyfikasi.domain.usecase.drive

import android.app.Activity
import dev.muffar.moneyfikasi.domain.model.DriveSignInResult
import dev.muffar.moneyfikasi.domain.repository.DriveBackupRepository

class SignInToDrive(
    private val repository: DriveBackupRepository
) {

    suspend operator fun invoke(activity: Activity? = null): DriveSignInResult {
        return try {
            repository.signIn(activity)
        } catch (e: Exception) {
            DriveSignInResult.Failure(e.message)
        }
    }
}
