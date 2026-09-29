package dev.muffar.moneyfikasi.data.remote.drive

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
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
    val signInClient: GoogleSignInClient
) {
    fun getSignInIntent(): Intent = signInClient.signInIntent

    fun getSignedInAccount(): GoogleSignInAccount? =
        GoogleSignIn.getLastSignedInAccount(context)

    fun isSignedIn(): Boolean {
        val account = getSignedInAccount()
        return account != null && GoogleSignIn.hasPermissions(account, Scope(DriveScopes.DRIVE_APPDATA))
    }

    suspend fun signOut() = withContext(Dispatchers.IO) {
        try {
            signInClient.signOut().await()
        } catch (_: Exception) {
        }
    }

    suspend fun handleSignInResult(data: Intent?): Boolean = withContext(Dispatchers.IO) {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.await()
            GoogleSignIn.hasPermissions(account, Scope(DriveScopes.DRIVE_APPDATA))
        } catch (_: Exception) {
            false
        }
    }

    fun getCredential(account: GoogleSignInAccount): GoogleAccountCredential {
        return GoogleAccountCredential.usingOAuth2(
            context,
            listOf(DriveScopes.DRIVE_APPDATA)
        ).apply {
            selectedAccount = account.account
        }
    }

    companion object {
        fun buildSignInOptions(): GoogleSignInOptions {
            val builder = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(Scope(DriveScopes.DRIVE_APPDATA))
            val webClientId = BuildConfig.DRIVE_WEB_CLIENT_ID
            if (webClientId.isNotEmpty()) {
                builder.requestIdToken(webClientId)
            }
            return builder.build()
        }
    }
}
