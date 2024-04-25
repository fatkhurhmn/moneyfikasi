package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.utils.TransactionDateFilter
import dev.muffar.moneyfikasi.utils.capitalize
import dev.muffar.moneyfikasi.utils.toFormattedDateTime

@Composable
fun DateRangeFilterTab(
    modifier: Modifier = Modifier,
    filter: TransactionDateFilter,
    startDateMillis: Long,
    endDateMillis: Long,
    onFilterSelect: (TransactionDateFilter) -> Unit,
) {
    val options = TransactionDateFilter.entries

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth(),
    ) {
        options.forEach { mFilter ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onFilterSelect(mFilter)
                    }
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = mFilter.name.capitalize(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    RadioButton(
                        selected = mFilter == filter,
                        onClick = { onFilterSelect(mFilter) }
                    )
                }

                AnimatedVisibility(
                    visible = mFilter == TransactionDateFilter.CUSTOM &&
                            filter == TransactionDateFilter.CUSTOM &&
                            startDateMillis != 0L &&
                            endDateMillis != 0L
                ) {
                    val start = startDateMillis.toFormattedDateTime("dd/MMM/yyyy")
                    val end = endDateMillis.toFormattedDateTime("dd/MMM/yyyy")
                    Text(
                        text = "$start - $end",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
        }
    }
}