package dev.muffar.moneyfikasi.recurring_transaction.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.EmptyDataList
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonAddButton
import dev.muffar.moneyfikasi.common_ui.component.tabs.IncomeExpenseTabs
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.domain.model.RecurringTransaction
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.recurring_transaction.list.component.RecurringTransactionItem
import dev.muffar.moneyfikasi.resource.R
import java.util.UUID

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecurringTransactionsScreen(
    modifier: Modifier = Modifier,
    state: RecurringTransactionsState,
    onAddRecurringTransactionClick: (TransactionType) -> Unit,
    onRecurringTransactionClick: (UUID) -> Unit,
    onToggleActive: (RecurringTransaction) -> Unit,
    onBackClick: () -> Unit,
) {
    val incomeRecurring = state.recurringTransactions.filter { it.type == TransactionType.INCOME }
    val expenseRecurring = state.recurringTransactions.filter { it.type == TransactionType.EXPENSE }
    val pagerState = rememberPagerState { state.tabs.size }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.recurring_transactions),
                onBackClick = onBackClick,
                titleSize = 20.sp
            )
        },
        floatingActionButton = {
            CommonAddButton(
                onClick = {
                    val currentTab = pagerState.currentPage
                    val type = TransactionType.valueOf(state.tabs[currentTab])
                    onAddRecurringTransactionClick(type)
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        IncomeExpenseTabs(
            modifier = modifier.padding(paddingValues),
            pagerState = pagerState
        ) { index ->
            val list = if (index == 0) incomeRecurring else expenseRecurring
            RecurringTransactionsContent(
                modifier = Modifier.fillMaxSize(),
                recurringTransactions = list,
                onClick = onRecurringTransactionClick,
                onToggleActive = onToggleActive
            )
        }
    }
}

@Composable
private fun RecurringTransactionsContent(
    modifier: Modifier = Modifier,
    recurringTransactions: List<RecurringTransaction>,
    onClick: (UUID) -> Unit,
    onToggleActive: (RecurringTransaction) -> Unit,
) {
    if (recurringTransactions.isNotEmpty()) {
        LazyColumn(
            modifier = modifier.background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(recurringTransactions, key = { it.id }) { recurringTransaction ->
                RecurringTransactionItem(
                    recurringTransaction = recurringTransaction,
                    onClick = onClick,
                    onToggleActive = onToggleActive
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
