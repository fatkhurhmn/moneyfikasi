package dev.muffar.moneyfikasi.export

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.text_input.DateInput
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.toMilliseconds
import dev.muffar.moneyfikasi.utils.extensions.LongExt.toLocalDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportScreen(
    state: ExportState,
    eventFlow: Flow<ExportViewModel.UiEvent>,
    onEvent: (ExportEvent) -> Unit,
    onExportTransactions: (Uri) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("*/*")
    ) { uri ->
        uri?.let { onExportTransactions(it) }
    }

    LaunchedEffect(eventFlow) {
        eventFlow.collectLatest { event ->
            when (event) {
                is ExportViewModel.UiEvent.SaveFile -> {
                    val mimeType = if (event.format == ExportFormat.CSV) "text/csv" else "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                    createDocumentLauncher.launch(event.filename)
                }
            }
        }
    }

    LaunchedEffect(state.message) {
        state.message?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.export),
                onBackClick = onBackClick
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.export_note),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            DateInput(
                date = state.startDate.toMilliseconds(),
                modifier = Modifier.fillMaxWidth(),
                onDateSelect = { onEvent(ExportEvent.OnStartDateChanged(it.toLocalDateTime())) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            DateInput(
                date = state.endDate.toMilliseconds(),
                modifier = Modifier.fillMaxWidth(),
                onDateSelect = { onEvent(ExportEvent.OnEndDateChanged(it.toLocalDateTime())) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.export_format),
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                RadioButton(
                    selected = state.format == ExportFormat.CSV,
                    onClick = { onEvent(ExportEvent.OnFormatChanged(ExportFormat.CSV)) }
                )
                Text(text = "CSV")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = state.format == ExportFormat.XLSX,
                    onClick = { onEvent(ExportEvent.OnFormatChanged(ExportFormat.XLSX)) }
                )
                Text(text = "Excel (.xlsx)")
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onEvent(ExportEvent.OnExportClick) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isExporting
            ) {
                Text(text = if (state.isExporting) stringResource(R.string.loading) else stringResource(R.string.export))
            }
        }
    }
}
