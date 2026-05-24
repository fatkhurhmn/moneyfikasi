package dev.muffar.moneyfikasi.recurring_transaction.add_edit.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.RecurringEndType
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.recurring_transaction.add_edit.AddEditRecurringTransactionState
import dev.muffar.moneyfikasi.utils.extensions.LongExt.toFormattedDateTime

@Composable
fun RecurringSummary(
    modifier: Modifier = Modifier,
    state: AddEditRecurringTransactionState
) {
    val startStr = state.startDate.toFormattedDateTime("MMM dd, yyyy")
    val freqStr = state.frequency.name.lowercase()

    val endStr = when (state.endType) {
        RecurringEndType.NEVER -> ""
        RecurringEndType.ON_DATE -> stringResource(
            R.string.until,
            state.endDate.toFormattedDateTime("MMM dd, yyyy")
        )

        RecurringEndType.AFTER_OCCURRENCES -> if (state.occurrenceCount.isNotEmpty()) {
            stringResource(R.string.for_times, state.occurrenceCount)
        } else {
            ""
        }
    }

    val summary = stringResource(
        R.string.repeats_from,
        freqStr,
        startStr,
        endStr
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Repeat,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
