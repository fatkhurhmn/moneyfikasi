package dev.muffar.moneyfikasi.export.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.ExportFormat
import dev.muffar.moneyfikasi.resource.R

@Composable
fun ExportFormatRadioGroup(
    selected: ExportFormat,
    onFormatChanged: (ExportFormat) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.label_export_format),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            ExportFormatItem(
                title = stringResource(R.string.label_csv),
                description = stringResource(R.string.msg_csv_description),
                selected = selected == ExportFormat.CSV,
                onClick = { onFormatChanged(ExportFormat.CSV) },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            ExportFormatItem(
                title = stringResource(R.string.label_xlsx),
                description = stringResource(R.string.msg_xlsx_description),
                selected = selected == ExportFormat.XLSX,
                onClick = { onFormatChanged(ExportFormat.XLSX) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}
