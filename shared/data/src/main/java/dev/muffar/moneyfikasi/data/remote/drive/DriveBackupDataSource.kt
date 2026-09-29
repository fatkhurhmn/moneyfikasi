package dev.muffar.moneyfikasi.data.remote.drive

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAuthIOException
import com.google.api.client.http.FileContent
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import dev.muffar.moneyfikasi.domain.model.DriveBackupFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DriveBackupDataSource @Inject constructor(
    private val authHelper: DriveAuthHelper
) {
    private fun requireDrive(): Drive {
        val account = authHelper.getSignedInAccount()
            ?: throw IllegalStateException("Not signed in to Google Drive")
        val credential = authHelper.getCredential(account)
        return Drive.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            credential
        ).setApplicationName("Moneyfikasi").build()
    }

    suspend fun listBackups(): List<DriveBackupFile> = withContext(Dispatchers.IO) {
        try {
            val drive = requireDrive()
            val result = drive.files().list()
                .setSpaces("appDataFolder")
                .setFields("files(id, name, modifiedTime, size)")
                .setOrderBy("modifiedTime desc")
                .setPageSize(50)
                .execute()
            result.files.orEmpty()
                .filter { it.name.endsWith(".zip") }
                .map {
                    DriveBackupFile(
                        id = it.id,
                        name = it.name,
                        modifiedTime = try {
                            it.modifiedTime?.value ?: 0L
                        } catch (_: Exception) {
                            0L
                        },
                        size = it.getSize() ?: 0L
                    )
                }
        } catch (e: GoogleAuthIOException) {
            throw IllegalStateException("Drive auth expired, please sign in again", e)
        }
    }

    suspend fun upload(zipFile: File, fileName: String): DriveBackupFile =
        withContext(Dispatchers.IO) {
            val drive = requireDrive()
            val metadata = com.google.api.services.drive.model.File().apply {
                name = fileName
                parents = listOf("appDataFolder")
            }
            val media = FileContent("application/zip", zipFile)
            val uploaded = drive.files().create(metadata, media)
                .setFields("id, name, modifiedTime, size")
                .execute()
            DriveBackupFile(
                id = uploaded.id,
                name = uploaded.name,
                modifiedTime = try {
                    uploaded.modifiedTime?.value ?: System.currentTimeMillis()
                } catch (_: Exception) {
                    System.currentTimeMillis()
                },
                size = uploaded.getSize() ?: zipFile.length()
            )
        }

    suspend fun downloadTo(fileId: String, destFile: File) = withContext(Dispatchers.IO) {
        val drive = requireDrive()
        drive.files().get(fileId).executeMediaAndDownloadTo(
            FileOutputStream(destFile)
        )
        Unit
    }

    suspend fun delete(fileId: String) = withContext(Dispatchers.IO) {
        requireDrive().files().delete(fileId).execute()
        Unit
    }
}
