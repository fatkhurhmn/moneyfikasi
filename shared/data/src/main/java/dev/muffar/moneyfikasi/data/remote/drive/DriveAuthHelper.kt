package dev.muffar.moneyfikasi.data.remote.drive

import android.accounts.Account
import android.app.Activity
import android.content.Context
import android.util.Log
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

    suspend fun signInWithCredentialManager(activityContext: Activity? = null): SignInResult =
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
                val authResult = getDriveAuthorizationResult()
                if (authResult.hasResolution()) {
                    SignInResult.NeedsAuthorization(authResult.pendingIntent)
                } else if (authResult.grantedScopes.isNotEmpty()) {
                    SignInResult.Success
                } else {
                    // No resolution but also not granted -> need consent
                    SignInResult.NeedsAuthorization(authResult.pendingIntent)
                }
            } catch (e: GetCredentialException) {
                Log.e("DriveAuthHelper", "Error getting credential", e)
                SignInResult.Failure(e.message)
            } catch (e: Exception) {
                Log.e("DriveAuthHelper", "Error signing in to Drive", e)
                SignInResult.Failure(e.message)
            }
        }

    suspend fun getDriveAuthorizationResult(): com.google.android.gms.auth.api.identity.AuthorizationResult =
        withContext(Dispatchers.IO) {
            val requestedScopes = listOf(Scope(DriveScopes.DRIVE_APPDATA))
            val authRequest = AuthorizationRequest.builder()
                .setRequestedScopes(requestedScopes)
                .build()
            authorizationClient.authorize(authRequest).await()
        }

    suspend fun handleAuthorizationResult(granted: Boolean): Boolean =
        withContext(Dispatchers.IO) {
            granted
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

    sealed class SignInResult {
        data object Success : SignInResult()
        data class NeedsAuthorization(val pendingIntent: android.app.PendingIntent?) : SignInResult()
        data class Failure(val message: String? = null) : SignInResult()
    }
}
