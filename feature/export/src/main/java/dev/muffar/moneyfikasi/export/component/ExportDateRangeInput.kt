package dev.muffar.moneyfikasi.export.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.text_input.DateInput
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
        DateInput(
            date = startDate.toMilliseconds(),
            label = stringResource(R.string.start_date),
            onDateSelect = onStartDateChanged
        )

        Spacer(modifier = Modifier.height(16.dp))

        DateInput(
            date = endDate.toMilliseconds(),
            label = stringResource(R.string.end_date),
            onDateSelect = onEndDateChanged
        )
    }
}