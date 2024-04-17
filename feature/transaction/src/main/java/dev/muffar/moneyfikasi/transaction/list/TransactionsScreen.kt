package dev.muffar.moneyfikasi.transaction.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.utils.TransactionFilter
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsFilterSection
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsFilterSheet
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsList
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsLoading
import dev.muffar.moneyfikasi.transaction.list.component.TransactionsTopBar
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    modifier: Modifier = Modifier,
    state: TransactionsState,
    onTransactionItemClick: (UUID) -> Unit,
    onFilterChanged: (TransactionFilter) -> Unit,
    onDateRangeChange: (Long, Long) -> Unit,
    onShowBottomSheet: () -> Unit,
    onCloseBottomSheet: () -> Unit,
) {

    Scaffold(
        topBar = {
            TransactionsTopBar(
                modifier = Modifier.padding(16.dp),
                onFilterClick = onShowBottomSheet
            )
        },
        contentWindowInsets = WindowInsets(0.dp)
    ) {
        Column(
            modifier = modifier.padding(it)
        ) {
            TransactionsFilterSection(
                filter = state.filter,
                onDateChange = onDateRangeChange
            )

            if (state.transactionsByDate.isNotEmpty()) {
                val dates = state.transactionsByDate.keys.toList()
                val transactions = state.transactionsByDate.values.toList()

                TransactionsList(
                    dates = dates,
                    transactions = transactions,
                    onItemClick = onTransactionItemClick
                )
            } else {
                if (state.isLoading) {
                    TransactionsLoading()
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.TwoTone.List,
                            contentDescription = stringResource(R.string.no_transactions),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                            modifier = Modifier.size(100.dp)
                        )
                        Text(text = stringResource(R.string.no_transactions))
                    }
                }
            }

            if (state.showTransactionFilter) {
                ModalBottomSheet(
                    onDismissRequest = onCloseBottomSheet
                ) {
                    TransactionsFilterSheet(
                        filter = state.filter,
                        onFilterChanged = onFilterChanged,
                        onClose = onCloseBottomSheet,
                    )
                }
            }
        }
    }
}