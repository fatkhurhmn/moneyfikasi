package dev.muffar.moneyfikasi.recurring_transaction.add_edit.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import dev.muffar.moneyfikasi.domain.utils.RecurringScheduleCalculator
import dev.muffar.moneyfikasi.domain.utils.extension.labelRes
import dev.muffar.moneyfikasi.recurring_transaction.add_edit.AddEditRecurringTransactionState
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.LongExt.formattedDate

@Composable
fun RecurringSummary(
    modifier: Modifier = Modifier,
    state: AddEditRecurringTransactionState
) {
    val startStr = state.startDate.formattedDate()
    val freqStr = stringResource(state.frequency.labelRes()).lowercase()

    val endStr = when (state.endType) {
        RecurringEndType.NEVER -> ""
        RecurringEndType.ON_DATE -> stringResource(
            R.string.msg_until,
            state.endDate.formattedDate()
        )

        RecurringEndType.AFTER_OCCURRENCES -> if (state.occurrenceCount.isNotEmpty()) {
            stringResource(R.string.msg_for_times, state.occurrenceCount)
        } else {
            ""
        }
    }

    val summary = stringResource(
        R.string.msg_repeats_from,
        freqStr,
        startStr,
        endStr
    )

    val nextRunMillis = RecurringScheduleCalculator.initialNextRun(
        startDate = state.startDate,
        frequency = state.frequency,
        skipFirstRun = state.isSkipFirst
    )

    val nextRunStr = nextRunMillis.formattedDate()

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
        ) {
            Row(
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
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.msg_next_run, nextRunStr),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 32.dp)
            )
        }
    }
}
