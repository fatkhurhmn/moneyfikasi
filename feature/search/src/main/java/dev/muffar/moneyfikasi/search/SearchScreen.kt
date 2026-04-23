package dev.muffar.moneyfikasi.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import dev.muffar.moneyfikasi.common_ui.component.EmptyDataList
import dev.muffar.moneyfikasi.common_ui.component.text_input.SearchBar
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.search.component.TransactionsList
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
                modifier = Modifier.fillMaxWidth().padding(16.dp),
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
                    text = stringResource(R.string.type_to_search),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
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
                        painter = painterResource(id = R.drawable.ic_search_not_found),
                        title = stringResource(id = R.string.no_search_results),
                        description = stringResource(id = R.string.no_search_results_message)
                    )
                }
            }
        }
    }
}
