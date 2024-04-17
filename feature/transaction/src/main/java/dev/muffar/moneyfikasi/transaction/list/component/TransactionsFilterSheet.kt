package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.domain.utils.TransactionFilter
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.capitalize

@Composable
fun TransactionsFilterSheet(
    modifier: Modifier = Modifier,
    filter: TransactionFilter,
    onClose: () -> Unit,
    onFilterChanged: (TransactionFilter) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.filter),
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 20.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        TransactionFilterRadioButton(
            filter = filter,
            onFilterSelect = {
                onFilterChanged(it)
                onClose()
            }
        )
    }
}

@Composable
fun TransactionFilterRadioButton(
    filter: TransactionFilter,
    onFilterSelect: (TransactionFilter) -> Unit,
) {
    val options = TransactionFilter.entries

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