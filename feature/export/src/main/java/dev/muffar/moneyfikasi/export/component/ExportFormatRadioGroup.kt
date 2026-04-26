package dev.muffar.moneyfikasi.export.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.button.RowRadioButton
import dev.muffar.moneyfikasi.domain.model.ExportFormat
import dev.muffar.moneyfikasi.resource.R

@Composable
fun ExportFormatRadioGroup(
    selected: ExportFormat,
    onFormatChanged: (ExportFormat) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.export_format),
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            RowRadioButton(
                label = stringResource(R.string.csv),
                selected = selected == ExportFormat.CSV,
                onClick = { onFormatChanged(ExportFormat.CSV) }
            )
            Spacer(modifier = Modifier.width(16.dp))
            RowRadioButton(
                label = stringResource(R.string.xlsx),
                selected = selected == ExportFormat.XLSX,
                onClick = { onFormatChanged(ExportFormat.XLSX) }
            )
        }
    }
}