package dev.muffar.moneyfikasi.transaction.detail

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.dialog.CommonAlertDialog
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarMessage
import dev.muffar.moneyfikasi.common_ui.component.message.showMessage
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.transaction.detail.component.TransactionDetailCard
import dev.muffar.moneyfikasi.transaction.detail.component.TransactionDetailSaveButton
import dev.muffar.moneyfikasi.transaction.detail.component.TransactionDetailTopBar
import dev.muffar.moneyfikasi.transaction.detail.component.TransferDetailCard
import dev.shreyaspatil.capturable.capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeApi::class)
@Composable
fun TransactionDetailScreen(
    state: TransactionDetailState,
    eventFlow: SharedFlow<TransactionDetailViewModel.UiEvent>,
    onEditClick: (TransactionType?, UUID) -> Unit,
    onDelete: () -> Unit,
    onShowAlert: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: (Bitmap) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val captureController = rememberCaptureController()
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarMessage(snackbarHostState) },
        topBar = {
            TransactionDetailTopBar(
                onEditClick = {
                    if (state.transactionId != null) {
                        onEditClick(state.transaction?.type, state.transactionId)
                    }
                },
                onDeleteClick = { onShowAlert(true) },
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            TransactionDetailSaveButton {
                scope.launch {
                    val bitmapAsync = captureController.captureAsync()
                    val bitmap = bitmapAsync.await()
                    onSaveClick(bitmap.asAndroidBitmap())
                }
            }
        }
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .padding(it)
                .capturable(captureController),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                if (state.isTransfer) {
                    if (state.transferDetail == null) return@Card
                    TransferDetailCard(state.transferDetail)
                } else {
                    if (state.transaction == null) return@Card
                    TransactionDetailCard(state.transaction)
                }
            }
        }
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

    LaunchedEffect(eventFlow) {
        eventFlow.collectLatest {
            when (it) {
                is TransactionDetailViewModel.UiEvent.DeleteTransaction -> onBackClick()
                is TransactionDetailViewModel.UiEvent.ShowMessage -> snackbarHostState.showMessage(
                    it.message,
                    it.type
                )
                is TransactionDetailViewModel.UiEvent.NavigateBack -> onBackClick()
            }
        }
    }
}