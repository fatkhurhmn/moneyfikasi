package dev.muffar.moneyfikasi.data.preferences.serializer

import androidx.datastore.core.Serializer
import dev.muffar.moneyfikasi.domain.model.NotificationSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object NotificationSettingsSerializer : Serializer<NotificationSettings> {
    override val defaultValue: NotificationSettings
        get() = NotificationSettings()

    override suspend fun readFrom(input: InputStream): NotificationSettings {
        return try {
            Json.decodeFromString(
                deserializer = NotificationSettings.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (_: SerializationException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: NotificationSettings, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = NotificationSettings.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
    }
}
