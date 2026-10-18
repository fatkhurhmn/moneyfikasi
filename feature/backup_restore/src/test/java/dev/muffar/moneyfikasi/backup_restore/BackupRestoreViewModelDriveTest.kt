package dev.muffar.moneyfikasi.backup_restore

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.muffar.moneyfikasi.data.remote.drive.DriveAuthHelper
import dev.muffar.moneyfikasi.domain.model.DriveSignInResult
import dev.muffar.moneyfikasi.domain.usecase.backup_restore.BackupRestoreUseCases
import dev.muffar.moneyfikasi.domain.usecase.drive.DriveBackupUseCases
import dev.muffar.moneyfikasi.domain.usecase.preferences.backup.BackupSettingsUseCases
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
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

    private lateinit var mockBackupRestoreUseCases: BackupRestoreUseCases
    private lateinit var mockBackupSettingsUseCases: BackupSettingsUseCases
    private lateinit var mockDriveBackupUseCases: DriveBackupUseCases
    private lateinit var viewModel: BackupRestoreViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockBackupRestoreUseCases = mockk(relaxed = true)
        mockBackupSettingsUseCases = mockk(relaxed = true)
        mockDriveBackupUseCases = mockk(relaxed = true)

        every { mockBackupSettingsUseCases.getBackupSettings() } returns flowOf(
            dev.muffar.moneyfikasi.domain.model.BackupSettings()
        )

        viewModel = BackupRestoreViewModel(
            backupRestoreUseCases = mockBackupRestoreUseCases,
            backupSettingsUseCases = mockBackupSettingsUseCases,
            driveBackupUseCases = mockDriveBackupUseCases
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `requestDriveSignIn failure and not signed in stays signed out`() = runTest {
        coEvery { mockDriveBackupUseCases.signInToDrive(any()) } returns DriveSignInResult.Failure("no cred")
        coEvery { mockDriveBackupUseCases.isDriveSignedIn() } returns false

        viewModel.requestDriveSignIn(null)
        advanceUntilIdle()

        coVerify { mockDriveBackupUseCases.signInToDrive(any()) }
        // Should not crash, and should stay not signed in
        assertFalse(viewModel.state.value.isDriveSignedIn)
    }

    @Test
    fun `requestDriveSignIn success refreshes status`() = runTest {
        coEvery { mockDriveBackupUseCases.signInToDrive(any()) } returns DriveSignInResult.Success
        coEvery { mockDriveBackupUseCases.isDriveSignedIn() } returns true
        every { mockDriveBackupUseCases.getDriveAccountEmail() } returns "user@test.com"
        coEvery { mockDriveBackupUseCases.getDriveBackups() } returns Result.success(emptyList())

        viewModel.requestDriveSignIn(null)
        advanceUntilIdle()

        assertTrue(viewModel.state.value.isDriveSignedIn)
        assertEquals("user@test.com", viewModel.state.value.driveAccountEmail)
    }

    @Test
    fun `signOut clears state`() = runTest {
        coEvery { mockDriveBackupUseCases.isDriveSignedIn() } returns true
        every { mockDriveBackupUseCases.getDriveAccountEmail() } returns "user@test.com"
        coEvery { mockDriveBackupUseCases.getDriveBackups() } returns Result.success(emptyList())

        // Sign in first so state is populated
        coEvery { mockDriveBackupUseCases.signInToDrive(any()) } returns DriveSignInResult.Success
        viewModel.requestDriveSignIn(null)
        advanceUntilIdle()
        assertTrue(viewModel.state.value.isDriveSignedIn)

        viewModel.onEvent(BackupRestoreEvent.DriveSignOut)
        advanceUntilIdle()

        coVerify { mockDriveBackupUseCases.driveSignOut() }
        assertFalse(viewModel.state.value.isDriveSignedIn)
        assertEquals("", viewModel.state.value.driveAccountEmail)
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
