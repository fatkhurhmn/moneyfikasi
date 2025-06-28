package dev.muffar.moneyfikasi.transaction.detail

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonAlertDialog
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.transaction.detail.component.TransactionDetailAmount
import dev.muffar.moneyfikasi.transaction.detail.component.TransactionDetailBody
import dev.muffar.moneyfikasi.transaction.detail.component.TransactionDetailDivider
import dev.muffar.moneyfikasi.transaction.detail.component.TransactionDetailHeader
import dev.muffar.moneyfikasi.transaction.detail.component.TransactionDetailNote
import dev.muffar.moneyfikasi.transaction.detail.component.TransactionDetailSaveButton
import dev.muffar.moneyfikasi.transaction.detail.component.TransactionDetailTopBar
import dev.shreyaspatil.capturable.capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.UUID

@OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeApi::class)
@Composable
fun TransactionDetailScreen(
    modifier: Modifier = Modifier,
    state: TransactionDetailState,
    eventFlow: SharedFlow<TransactionDetailViewModel.UiEvent>,
    onEdit: (TransactionType, UUID) -> Unit,
    onDelete: () -> Unit,
    onShowAlert: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: (Bitmap) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val captureController = rememberCaptureController()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(eventFlow) {
        eventFlow.collectLatest {
            when (it) {
                is TransactionDetailViewModel.UiEvent.DeleteTransaction -> onBackClick()
                is TransactionDetailViewModel.UiEvent.ShowMessage ->
                    snackbarHostState.showSnackbar(it.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TransactionDetailTopBar(
                transaction = state.transaction,
                onEditClick = onEdit,
                onDeleteClick = { onShowAlert(true) },
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            TransactionDetailSaveButton {
                scope.launch {
                    val bitmapAsync = captureController.captureAsync()
                    try {
                        val bitmap = bitmapAsync.await()
                        onSaveClick(bitmap.asAndroidBitmap())
                    } catch (error: Throwable) {
                        error.printStackTrace()
                        Toast.makeText(context, "Failed to save summary", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    ) {
        Card(
            modifier = modifier
                .padding(16.dp)
                .padding(it)
                .capturable(captureController),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(0.5f)),
        ) {
            Column(
                modifier = modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                TransactionDetailHeader(state.transaction?.type)
                Spacer(modifier = Modifier.height(32.dp))
                TransactionDetailAmount(
                    amount = state.transaction?.amount,
                    type = state.transaction?.type
                )
                TransactionDetailDivider()
                TransactionDetailBody(
                    date = state.transaction?.date,
                    wallet = state.transaction?.wallet,
                    category = state.transaction?.category,
                )

                val note = state.transaction?.note
                if (!note.isNullOrBlank()) {
                    TransactionDetailDivider()
                    TransactionDetailNote(note)
                }

                if (state.showAlert) {
                    CommonAlertDialog(
                        title = stringResource(R.string.delete_transaction),
                        message = stringResource(R.string.delete_transaction_confirmation),
                        positiveText = stringResource(R.string.delete),
                        negativeText = stringResource(R.string.cancel),
                        onDismiss = { onShowAlert(false) },
                        onConfirm = {
                            onDelete()
                            onShowAlert(false)
                        }
                    )
                }
            }
        }
    }
}

fun saveBitmapToGallery(
    context: Context,
    bitmap: Bitmap,
    displayName: String = "image_${System.currentTimeMillis()}"
) {
    val filename = "$displayName.jpg"

    val fos: OutputStream?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // Android 10+ (Scoped Storage)
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        fos = imageUri?.let { resolver.openOutputStream(it) }

        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }

        contentValues.clear()
        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
        imageUri?.let { resolver.update(it, contentValues, null, null) }
    } else {
        // Android 9 ke bawah
        val imagesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
        val file = File(imagesDir, filename)
        fos = FileOutputStream(file)
        fos.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }

        // Update galeri
        MediaScannerConnection.scanFile(context, arrayOf(file.toString()), null, null)
    }

    Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_SHORT).show()
}