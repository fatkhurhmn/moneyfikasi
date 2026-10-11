package dev.muffar.moneyfikasi.debt.list

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonAddButton
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.debt.list.component.DebtsContent
import dev.muffar.moneyfikasi.resource.R
import java.util.UUID

@Composable
fun DebtsScreen(
    modifier: Modifier = Modifier,
    state: DebtsState,
    onDebtClick: (UUID) -> Unit,
    onAddDebtClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.title_debts),
                onBackClick = onBackClick
            )
        },
        floatingActionButton = {
            CommonAddButton(onClick = onAddDebtClick)
        }
    ) { paddingValues ->
        DebtsContent(
            modifier = modifier.padding(paddingValues),
            debts = state.debts,
            onClick = onDebtClick
        )
    }
}
