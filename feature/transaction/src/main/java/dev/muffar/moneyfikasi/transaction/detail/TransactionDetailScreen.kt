package dev.muffar.moneyfikasi.transaction.detail

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
import dev.muffar.moneyfikasi.transaction.detail.component.TransactionDetailTopBar
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import java.util.UUID

@Composable
fun TransactionDetailScreen(
    modifier: Modifier = Modifier,
    state: TransactionDetailState,
    eventFlow: SharedFlow<TransactionDetailViewModel.UiEvent>,
    onEdit: (TransactionType, UUID) -> Unit,
    onDelete: () -> Unit,
    onShowAlert: (Boolean) -> Unit,
    onBackClick: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

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
    ) {
        Card(
            modifier = modifier
                .padding(16.dp)
                .padding(it),
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