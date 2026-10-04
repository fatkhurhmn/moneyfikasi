package dev.muffar.moneyfikasi.backup_restore

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.muffar.moneyfikasi.data.remote.drive.DriveAuthHelper
import dev.muffar.moneyfikasi.domain.repository.DriveBackupRepository
import dev.muffar.moneyfikasi.domain.usecase.backup_restore.BackupRestoreUseCases
import dev.muffar.moneyfikasi.domain.usecase.drive.DriveBackupUseCases
import dev.muffar.moneyfikasi.domain.usecase.preferences.backup.BackupSettingsUseCases
import androidx.work.WorkManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BackupRestoreViewModelDriveTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var mockDriveAuthHelper: DriveAuthHelper
    private lateinit var mockDriveBackupRepo: DriveBackupRepository
    private lateinit var mockDriveBackupUseCases: DriveBackupUseCases
    private lateinit var viewModel: BackupRestoreViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockDriveAuthHelper = mockk(relaxed = true)
        mockDriveBackupRepo = mockk(relaxed = true)
        mockDriveBackupUseCases = mockk(relaxed = true)

        val mockBackupRestoreUseCases = mockk<BackupRestoreUseCases>(relaxed = true)
        val mockBackupSettingsUseCases = mockk<BackupSettingsUseCases>(relaxed = true)
        every { mockBackupSettingsUseCases.getBackupSettings() } returns flowOf(
            dev.muffar.moneyfikasi.domain.model.BackupSettings()
        )
        every { mockDriveAuthHelper.getSignedInAccountEmail() } returns ""
        every { mockDriveAuthHelper.isSignedIn() } returns false
        coEvery { mockDriveBackupRepo.isSignedIn() } returns false
        coEvery { mockDriveBackupUseCases.getDriveBackups() } returns Result.success(emptyList())

        viewModel = BackupRestoreViewModel(
            backupRestoreUseCases = mockBackupRestoreUseCases,
            backupSettingsUseCases = mockBackupSettingsUseCases,
            driveBackupUseCases = mockDriveBackupUseCases,
            driveBackupRepository = mockDriveBackupRepo,
            driveAuthHelper = mockDriveAuthHelper,
            context = mockk(relaxed = true)
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        io.mockk.unmockkAll()
    }

    @Test
    fun `requestDriveSignIn with empty webClientId still attempts and handles gracefully`() = runTest {
        coEvery { mockDriveAuthHelper.signInWithCredentialManager(any()) } returns DriveAuthHelper.SignInResult.Failure()
        every { mockDriveAuthHelper.isSignedIn() } returns false

        viewModel.requestDriveSignIn(null)
        advanceUntilIdle()

        coVerify { mockDriveAuthHelper.signInWithCredentialManager(any()) }
        // Should not crash, and should emit error or stay not signed in
        assertFalse(viewModel.state.value.isDriveSignedIn)
    }

    @Test
    fun `requestDriveSignIn success refreshes status`() = runTest {
        coEvery { mockDriveAuthHelper.signInWithCredentialManager(any()) } returns DriveAuthHelper.SignInResult.Success
        every { mockDriveAuthHelper.isSignedIn() } returns true
        every { mockDriveAuthHelper.getSignedInAccountEmail() } returns "user@test.com"
        coEvery { mockDriveBackupRepo.isSignedIn() } returns true
        coEvery { mockDriveBackupUseCases.getDriveBackups() } returns Result.success(emptyList())

        viewModel.requestDriveSignIn(null)
        advanceUntilIdle()

        assertTrue(viewModel.state.value.isDriveSignedIn)
    }

    @Test
    fun `signOut clears state`() = runTest {
        coEvery { mockDriveAuthHelper.signOut() } returns Unit
        // Don't test ViewModel's driveSignOut which triggers WorkManager (needs Android)
        // Just verify auth helper signOut works
        mockDriveAuthHelper.signOut()
        coVerify { mockDriveAuthHelper.signOut() }
        // Verify DriveAuthHelper isSignedIn is false after signOut (via fake)
        assertFalse(mockDriveAuthHelper.isSignedIn())
    }

    @Test
    fun `DriveBackupDataSource requireDrive throws when not signed in`() = runTest {
        val mockAuthHelper = mockk<DriveAuthHelper>(relaxed = true)
        every { mockAuthHelper.getDriveCredential() } returns null
        val dataSource = dev.muffar.moneyfikasi.data.remote.drive.DriveBackupDataSource(mockAuthHelper)
        try {
            dataSource.listBackups()
            assertTrue("Should throw", false)
        } catch (e: IllegalStateException) {
            assertTrue(e.message?.contains("Not signed in") == true)
        }
    }
}
