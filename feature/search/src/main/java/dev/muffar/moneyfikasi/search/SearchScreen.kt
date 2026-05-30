package dev.muffar.moneyfikasi.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import dev.muffar.moneyfikasi.common_ui.component.EmptyDataList
import dev.muffar.moneyfikasi.common_ui.component.text_input.SearchBar
import dev.muffar.moneyfikasi.common_ui.component.transaction.TransactionsList
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDateTime
import java.util.UUID

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    state: SearchState,
    onQueryChange: (String) -> Unit,
    onNavigateToTransactionDetail: (UUID, Boolean) -> Unit,
    onBackClick: () -> Unit,
    onGetDailyBalance: (LocalDateTime) -> Flow<Double>,
) {
    val transactions = state.transactions.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .statusBarsPadding(),
                searchQuery = state.searchQuery ?: "",
                onBackClick = onBackClick,
                onQueryChange = onQueryChange,
            )
        }
    ) {
        Box(
            modifier = modifier
                .padding(it)
                .consumeWindowInsets(it)
                .imePadding()
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (state.searchQuery.isNullOrEmpty()) {
                Text(
                    text = stringResource(R.string.label_type_to_search),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                if (transactions.itemCount > 0) {
                    TransactionsList(
                        transactions = transactions,
                        onItemClick = { id, isTransfer ->
                            onNavigateToTransactionDetail(
                                id,
                                isTransfer,
                            )
                        },
                        onGetDailyBalance = onGetDailyBalance,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    EmptyDataList(
                        title = stringResource(id = R.string.empty_search_results_title),
                        description = stringResource(id = R.string.empty_search_results_msg)
                    )
                }
            }
        }
    }
}
