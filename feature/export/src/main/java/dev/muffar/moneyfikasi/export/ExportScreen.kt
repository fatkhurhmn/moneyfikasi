package dev.muffar.moneyfikasi.export

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.button.BottomBarButton
import dev.muffar.moneyfikasi.common_ui.component.dialog.LoadingDialog
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarMessage
import dev.muffar.moneyfikasi.common_ui.component.message.showMessage
import dev.muffar.moneyfikasi.domain.model.ExportFormat
import dev.muffar.moneyfikasi.export.component.ExportDateRangeInput
import dev.muffar.moneyfikasi.export.component.ExportFormatRadioGroup
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportScreen(
    state: ExportState,
    eventFlow: Flow<ExportViewModel.UiEvent>,
    onStartDateChanged: (Long) -> Unit,
    onEndDateChanged: (Long) -> Unit,
    onFormatChanged: (ExportFormat) -> Unit,
    onExportTransactions: (Uri) -> Unit,
    onBackClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("*/*")
    ) { uri ->
        uri?.let { onExportTransactions(it) }
    }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.export),
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            BottomBarButton(
                title = stringResource(R.string.export),
                onClick = {
                    createDocumentLauncher.launch(state.fileName)
                }
            )
        },
        snackbarHost = { SnackbarMessage(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            ExportDateRangeInput(
                startDate = state.startDate,
                endDate = state.endDate,
                onStartDateChanged = onStartDateChanged,
                onEndDateChanged = onEndDateChanged
            )
            Spacer(modifier = Modifier.height(24.dp))
            ExportFormatRadioGroup(
                selected = state.format,
                onFormatChanged = onFormatChanged
            )
        }
    }

    if (state.isLoading){
        LoadingDialog()
    }

    LaunchedEffect(eventFlow) {
        eventFlow.collectLatest { event ->
            when (event) {
                is ExportViewModel.UiEvent.ShowMessage -> {
                    snackbarHostState.showMessage(event.message, event.type)
                }
            }
        }
    }
}
