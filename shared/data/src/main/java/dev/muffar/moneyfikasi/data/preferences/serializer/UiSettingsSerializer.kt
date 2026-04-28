package dev.muffar.moneyfikasi.data.preferences.serializer

import androidx.datastore.core.Serializer
import dev.muffar.moneyfikasi.domain.model.UiSettings
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object UiSettingsSerializer : Serializer<UiSettings> {
    override val defaultValue: UiSettings
        get() = UiSettings()

    override suspend fun readFrom(input: InputStream): UiSettings {
        return try {
            Json.decodeFromString(
                deserializer = UiSettings.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (_: SerializationException) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: UiSettings, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = UiSettings.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}
