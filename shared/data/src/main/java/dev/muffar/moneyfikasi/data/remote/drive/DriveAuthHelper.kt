package dev.muffar.moneyfikasi.data.remote.drive

import android.accounts.Account
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.gms.auth.api.identity.AuthorizationClient
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.services.drive.DriveScopes
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.muffar.moneyfikasi.data.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DriveAuthHelper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val credentialManager: CredentialManager,
    private val authorizationClient: AuthorizationClient
) {
    @Volatile
    private var cachedAccount: Account? = null

    fun isSignedIn(): Boolean = cachedAccount != null

    fun getSignedInAccountEmail(): String = cachedAccount?.name.orEmpty()

    suspend fun signInWithCredentialManager(activityContext: android.app.Activity? = null): Boolean = withContext(Dispatchers.IO) {
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
            val credential = com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.createFrom(result.credential.data)
            cachedAccount = Account(credential.id, "com.google")
            requestDriveAuthorization(activityContext)
        } catch (e: GetCredentialException) {
            false
        } catch (_: Exception) {
            false
        }
    }

    private suspend fun requestDriveAuthorization(activityContext: android.app.Activity? = null): Boolean = withContext(Dispatchers.IO) {
        try {
            val requestedScopes = listOf(com.google.android.gms.common.api.Scope(DriveScopes.DRIVE_APPDATA))
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
        } catch (_: Exception) {
        }
    }

    fun getDriveCredential(): GoogleAccountCredential? {
        val account = cachedAccount ?: return null
        return GoogleAccountCredential.usingOAuth2(
            context,
            listOf(DriveScopes.DRIVE_APPDATA)
        ).apply {
            selectedAccount = account
        }
    }
}
