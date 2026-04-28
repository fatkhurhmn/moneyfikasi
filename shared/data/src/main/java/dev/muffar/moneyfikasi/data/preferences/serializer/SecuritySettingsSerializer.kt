package dev.muffar.moneyfikasi.data.preferences.serializer

import androidx.datastore.core.Serializer
import dev.muffar.moneyfikasi.domain.model.SecuritySettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object SecuritySettingsSerializer : Serializer<SecuritySettings> {
    override val defaultValue: SecuritySettings
        get() = SecuritySettings()

    override suspend fun readFrom(input: InputStream): SecuritySettings {
        return try {
            Json.decodeFromString(
                deserializer = SecuritySettings.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (_: SerializationException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: SecuritySettings, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = SecuritySettings.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
    }
}
