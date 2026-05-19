package dev.muffar.moneyfikasi.recurring_transaction.list

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.EmptyDataList
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonAddButton
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.recurring_transaction.list.component.RecurringTransactionItem
import dev.muffar.moneyfikasi.resource.R
import java.util.UUID

@Composable
fun RecurringTransactionsScreen(
    modifier: Modifier = Modifier,
    state: RecurringTransactionsState,
    onAddRecurringTransactionClick: () -> Unit,
    onRecurringTransactionClick: (UUID) -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.recurring_transactions),
                onBackClick = onBackClick
            )
        },
        floatingActionButton = {
            CommonAddButton(
                onClick = onAddRecurringTransactionClick
            )
        }
    ) { paddingValues ->
        if (state.recurringTransactions.isNotEmpty()) {
            LazyColumn(
                modifier = modifier.padding(paddingValues)
            ) {
                items(state.recurringTransactions) { recurringTransaction ->
                    RecurringTransactionItem(
                        recurringTransaction = recurringTransaction,
                        onClick = onRecurringTransactionClick
                    )
                }
            }
        } else {
            EmptyDataList(
                title = stringResource(R.string.no_recurring),
                description = stringResource(R.string.no_recurring_message)
            )
        }
    }
}
