package dev.muffar.moneyfikasi.transaction.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
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
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.transaction.detail.component.TransactionDetailAction
import dev.muffar.moneyfikasi.transaction.detail.component.TransactionDetailBody
import dev.muffar.moneyfikasi.transaction.detail.component.TransactionDetailHeader
import dev.muffar.moneyfikasi.utils.format
import dev.muffar.moneyfikasi.utils.formatThousand
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import java.util.UUID

@Composable
fun TransactionDetailScreen(
    modifier: Modifier = Modifier,
    state: TransactionDetailState,
    eventFlow: SharedFlow<TransactionDetailViewModel.UiEvent>,
    onEdit: (UUID) -> Unit,
    onDelete: () -> Unit,
    onShowAlert: (Boolean) -> Unit,
    onBackClick: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val amount = state.transaction?.amount?.toLong()?.formatThousand() ?: ""
    val type = state.transaction?.type ?: TransactionType.INCOME
    val wallet = state.transaction?.wallet?.name ?: ""
    val category = state.transaction?.category?.name ?: ""
    val date = state.transaction?.date?.format("dd-MM-yyyy, kk:mm") ?: ""
    val note = state.transaction?.note ?: "-"

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
            CommonTopAppBar(
                title = stringResource(R.string.transaction),
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            TransactionDetailAction(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                onDeleteClick = { onShowAlert(true) },
                onEditClick = { onEdit(state.transaction!!.id) },
            )
        }
    ) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .padding(it)
                .fillMaxWidth()
                .background(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.background
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(0.2f),
                    shape = MaterialTheme.shapes.medium
                )
                .padding(16.dp)
        ) {
            TransactionDetailHeader(amount = amount, type = type)
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(0.2f),
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            )
            TransactionDetailBody(
                wallet = wallet,
                category = category,
                type = type.value,
                dateTime = date,
                note = note
            )

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