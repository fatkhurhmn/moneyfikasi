package dev.muffar.moneyfikasi.domain.usecase.transaction

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class SaveTransactionImage() {
    operator fun invoke(context: Context, bitmap: Bitmap) {
        val filename = "image_${System.currentTimeMillis()}.jpg"

        val fos: OutputStream?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }

            val imageUri =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUri?.let { resolver.openOutputStream(it) }

            fos?.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }

            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            imageUri?.let { resolver.update(it, contentValues, null, null) }
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .toString()
            val file = File(imagesDir, filename)
            fos = FileOutputStream(file)
            fos.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
            MediaScannerConnection.scanFile(context, arrayOf(file.toString()), null, null)
        }

        Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_SHORT).show()
    }
}
