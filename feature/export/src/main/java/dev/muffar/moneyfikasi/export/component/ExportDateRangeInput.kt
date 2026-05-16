package dev.muffar.moneyfikasi.export.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.toMilliseconds
import org.threeten.bp.LocalDateTime

@Composable
fun ExportDateRangeInput(
    startDate: LocalDateTime,
    endDate: LocalDateTime,
    onStartDateChanged: (Long) -> Unit,
    onEndDateChanged: (Long) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.date_range),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ExportDateItem(
            label = stringResource(R.string.start_date),
            date = startDate.toMilliseconds(),
            onDateSelect = onStartDateChanged
        )

        Spacer(modifier = Modifier.height(16.dp))

        ExportDateItem(
            label = stringResource(R.string.end_date),
            date = endDate.toMilliseconds(),
            onDateSelect = onEndDateChanged
        )
    }
}
