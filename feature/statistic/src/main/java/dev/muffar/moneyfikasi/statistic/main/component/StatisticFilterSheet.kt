package dev.muffar.moneyfikasi.statistic.main.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.utils.TransactionDateFilter
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.capitalize

@Composable
fun StatisticFilterSheet(
    modifier: Modifier = Modifier,
    filter: TransactionDateFilter,
    onClose: () -> Unit,
    onFilterChanged: (TransactionDateFilter) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.filter),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
            )
            IconButton(
                onClick = onClose
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        DateRangeFilterRadioButton(
            filter = filter,
            onFilterSelect = {
                onFilterChanged(it)
                if (it != TransactionDateFilter.CUSTOM) {
                    onClose()
                }
            }
        )
    }
}

@Composable
fun DateRangeFilterRadioButton(
    filter: TransactionDateFilter,
    onFilterSelect: (TransactionDateFilter) -> Unit,
) {
    val options = TransactionDateFilter.entries

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {
        options.forEach { mFilter ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onFilterSelect(mFilter)
                    }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = mFilter.name.capitalize(),
                    style = MaterialTheme.typography.bodyLarge
                )
                RadioButton(
                    selected = mFilter == filter,
                    onClick = {}
                )
            }
        }
    }
}