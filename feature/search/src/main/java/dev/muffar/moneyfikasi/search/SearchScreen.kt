package dev.muffar.moneyfikasi.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import dev.muffar.moneyfikasi.common_ui.component.EmptyDataList
import dev.muffar.moneyfikasi.common_ui.component.SearchBar
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.search.component.SearchTopBar
import dev.muffar.moneyfikasi.search.component.TransactionsList
import java.util.UUID

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    state: SearchState,
    onQueryChange: (String) -> Unit,
    onNavigateToTransactionDetail: (UUID) -> Unit,
) {

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                SearchTopBar()
                SearchBar(
                    searchQuery = state.searchQuery ?: "",
                    onQueryChange = onQueryChange
                )
            }
        }
    ) {
        Box(
            modifier = modifier
                .padding(it)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (state.searchQuery.isNullOrEmpty()) {
                Text(
                    text = stringResource(R.string.type_to_search),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
                return@Box
            } else {
                if (state.transactions.isNotEmpty()) {
                    TransactionsList(
                        dates = state.transactionsByDate.keys.toList(),
                        transactions = state.transactionsByDate.values.toList(),
                        onItemClick = { id -> onNavigateToTransactionDetail(id) },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    EmptyDataList(
                        painter = painterResource(id = R.drawable.ic_search_not_found),
                        description = stringResource(id = R.string.no_search_results)
                    )
                }
            }
        }
    }
}