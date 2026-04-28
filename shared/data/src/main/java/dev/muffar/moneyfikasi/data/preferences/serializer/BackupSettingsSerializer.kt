package dev.muffar.moneyfikasi.data.preferences.serializer

import androidx.datastore.core.Serializer
import dev.muffar.moneyfikasi.domain.model.BackupSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object BackupSettingsSerializer : Serializer<BackupSettings> {
    override val defaultValue: BackupSettings
        get() = BackupSettings()

    override suspend fun readFrom(input: InputStream): BackupSettings {
        return try {
            Json.decodeFromString(
                deserializer = BackupSettings.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (_: SerializationException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: BackupSettings, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = BackupSettings.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
    }
}
