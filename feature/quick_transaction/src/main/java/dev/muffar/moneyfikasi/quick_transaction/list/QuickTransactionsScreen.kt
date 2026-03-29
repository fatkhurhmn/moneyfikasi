package dev.muffar.moneyfikasi.quick_transaction.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.button.CommonAddButton
import dev.muffar.moneyfikasi.resource.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuickTransactionsScreen(
    state: QuickTransactionsState,
    onBackClick: () -> Unit,
) {

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.quick_transaction),
                onBackClick = onBackClick
            )
        },
        floatingActionButton = {
            CommonAddButton(
                onClick = { }
            )
        }
    ) {
    }
}
