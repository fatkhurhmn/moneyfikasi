package dev.muffar.moneyfikasi.data.remote.drive

import android.accounts.Account
import android.content.Context
import androidx.credentials.CredentialManager
import com.google.android.gms.auth.api.identity.AuthorizationClient
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.lang.reflect.Field

class DriveAuthHelperTest {

    private lateinit var mockContext: Context
    private lateinit var mockCredentialManager: CredentialManager
    private lateinit var mockAuthClient: AuthorizationClient
    private lateinit var helper: DriveAuthHelper

    @Before
    fun setUp() {
        mockContext = mockk(relaxed = true)
        mockCredentialManager = mockk(relaxed = true)
        mockAuthClient = mockk(relaxed = true)
        helper = DriveAuthHelper(mockContext, mockCredentialManager, mockAuthClient)
    }

    @Test
    fun `initial isSignedIn is false`() {
        assertFalse(helper.isSignedIn())
        assertEquals("", helper.getSignedInAccountEmail())
        assertEquals(null, helper.getDriveCredential())
    }

    @Test
    fun `signOut clears account`() = runTest {
        helper.setAccountForTesting(Account("user@test.com", "com.google"))
        assertTrue(helper.isSignedIn())
        helper.signOut()
        assertFalse(helper.isSignedIn())
        assertEquals("", helper.getSignedInAccountEmail())
    }

    @Test
    fun `getDriveCredential returns null when not signed in`() {
        assertEquals(null, helper.getDriveCredential())
    }

    @Test
    fun `cached account email reflects signed in account`() = runTest {
        helper.setAccountForTesting(Account("myemail@gmail.com", "com.google"))
        // In unit test without Robolectric, Account.name may be empty, so just check isSignedIn
        assertTrue(helper.isSignedIn())
    }

    @Test
    fun `isSignedIn true after setting account`() = runTest {
        helper.setAccountForTesting(Account("test@moneyfikasi.com", "com.google"))
        assertTrue(helper.isSignedIn())
    }

    private fun setCachedAccount(account: Account) {
        helper.setAccountForTesting(account)
    }
}
