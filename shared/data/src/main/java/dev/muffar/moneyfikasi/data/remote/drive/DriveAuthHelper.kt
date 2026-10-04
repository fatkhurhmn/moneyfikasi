package dev.muffar.moneyfikasi.data.remote.drive

import android.accounts.Account
import android.app.Activity
import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.gms.auth.api.identity.AuthorizationClient
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.common.api.Scope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.services.drive.DriveScopes
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.muffar.moneyfikasi.data.BuildConfig
import dev.muffar.moneyfikasi.data.preferences.BackupPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DriveAuthHelper @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val credentialManager: CredentialManager,
    private val authorizationClient: AuthorizationClient,
    private val backupPreferences: BackupPreferences
) {
    @Volatile
    private var cachedAccount: Account? = null

    init {
        restoreAccountFromStorage()
    }

    private fun restoreAccountFromStorage() {
        if (cachedAccount == null) {
            val savedEmail = runCatching {
                runBlocking { backupPreferences.backupSettings.first().driveAccountEmail }
            }.getOrNull()
            if (!savedEmail.isNullOrEmpty()) {
                cachedAccount = Account(savedEmail, "com.google")
            }
        }
    }

    fun isSignedIn(): Boolean {
        if (cachedAccount == null) {
            restoreAccountFromStorage()
        }
        return cachedAccount != null
    }

    fun getSignedInAccountEmail(): String {
        if (cachedAccount == null) {
            restoreAccountFromStorage()
        }
        return cachedAccount?.name.orEmpty()
    }

    @VisibleForTesting
    fun setAccountForTesting(account: Account?) {
        cachedAccount = account
        val email = account?.name.orEmpty()
        saveAccountEmail(email)
    }

    suspend fun signInWithCredentialManager(activityContext: Activity? = null): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val webClientId = BuildConfig.DRIVE_WEB_CLIENT_ID
                val googleIdBuilder = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                if (webClientId.isNotEmpty()) {
                    googleIdBuilder.setServerClientId(webClientId)
                }
                val googleIdOption = googleIdBuilder.build()
                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()
                val ctx = activityContext ?: context
                val result = credentialManager.getCredential(ctx, request)
                val credential = GoogleIdTokenCredential.createFrom(result.credential.data)
                cachedAccount = Account(credential.id, "com.google")
                saveAccountEmail(credential.id)
                requestDriveAuthorization(activityContext)
            } catch (e: GetCredentialException) {
                false
            } catch (_: Exception) {
                false
            }
        }

    private suspend fun requestDriveAuthorization(activityContext: Activity? = null): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val requestedScopes = listOf(Scope(DriveScopes.DRIVE_APPDATA))
                val authRequest = AuthorizationRequest.builder()
                    .setRequestedScopes(requestedScopes)
                    .build()
                val result = authorizationClient.authorize(authRequest).await()
                if (result.hasResolution()) {
                    val pendingIntent = result.pendingIntent
                    if (pendingIntent != null && activityContext != null) {
                        try {
                            activityContext.startIntentSenderForResult(
                                pendingIntent.intentSender,
                                1001, null, 0, 0, 0, null
                            )
                        } catch (_: Exception) {
                        }
                        return@withContext false
                    }
                    return@withContext false
                }
                result.grantedScopes.isNotEmpty()
            } catch (_: Exception) {
                false
            }
        }

    suspend fun signOut() = withContext(Dispatchers.IO) {
        try {
            cachedAccount = null
            saveAccountEmail("")
        } catch (_: Exception) {
        }
    }

    fun getDriveCredential(): GoogleAccountCredential? {
        if (cachedAccount == null) {
            restoreAccountFromStorage()
        }
        val account = cachedAccount ?: return null
        return GoogleAccountCredential.usingOAuth2(
            context,
            listOf(DriveScopes.DRIVE_APPDATA)
        ).apply {
            selectedAccount = account
        }
    }

    private fun saveAccountEmail(email: String) {
        try {
            runBlocking { backupPreferences.setDriveAccountEmail(email) }
        } catch (_: Exception) {
        }
    }
}
