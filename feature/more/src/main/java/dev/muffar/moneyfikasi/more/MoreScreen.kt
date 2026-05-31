package dev.muffar.moneyfikasi.more

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DataUsage
import androidx.compose.material.icons.rounded.PostAdd
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.Wallet
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.more.component.MoreItem
import dev.muffar.moneyfikasi.more.component.MoreTopBar
import dev.muffar.moneyfikasi.resource.R

@Composable
fun MoreScreen(
    modifier: Modifier = Modifier,
    state: MoreState,
    onWalletsClick: () -> Unit,
    onCategoriesClick: () -> Unit,
    onBudgetsClick: () -> Unit,
    onPresetsClick: () -> Unit,
    onRecurringTransactionsClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {

    Scaffold(
        topBar = {
            MoreTopBar(onSettingsClick = onSettingsClick)
        }
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = modifier.padding(it),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalItemSpacing = 8.dp,
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 100.dp
            )
        ) {
            item {
                MoreItem(
                    label = stringResource(R.string.title_wallets),
                    title = stringResource(R.string.label_my_wallet),
                    description = stringResource(R.string.msg_wallet_description),
                    status = stringResource(R.string.msg_wallets_count, state.activeWalletsCount),
                    icon = Icons.Rounded.Wallet,
                    onClick = onWalletsClick
                )
            }
            item {
                MoreItem(
                    label = stringResource(R.string.title_categories),
                    title = stringResource(R.string.label_manage_categories),
                    description = stringResource(R.string.msg_category_description),
                    status = stringResource(R.string.msg_categories_count, state.categoriesCount),
                    icon = Icons.Rounded.Widgets,
                    onClick = onCategoriesClick
                )
            }
            item {
                MoreItem(
                    label = stringResource(R.string.title_presets),
                    title = stringResource(R.string.label_transaction_presets),
                    description = stringResource(R.string.msg_preset_description),
                    status = stringResource(R.string.msg_presets_count, state.presetsCount),
                    icon = Icons.Rounded.PostAdd,
                    onClick = onPresetsClick
                )
            }
            item {
                MoreItem(
                    label = stringResource(R.string.title_recurring_transaction),
                    title = stringResource(R.string.label_scheduled_transactions),
                    description = stringResource(R.string.msg_recurring_description),
                    status = stringResource(
                        R.string.msg_recurring_count,
                        state.recurringTransactionsCount
                    ),
                    icon = Icons.Rounded.Repeat,
                    onClick = onRecurringTransactionsClick
                )
            }
            item {
                MoreItem(
                    label = stringResource(R.string.title_budgets),
                    title = stringResource(R.string.label_financial_goals),
                    description = stringResource(R.string.msg_financial_goals_description),
                    status = stringResource(R.string.msg_budgets_count, state.budgetsCount),
                    icon = Icons.Rounded.DataUsage,
                    onClick = onBudgetsClick
                )
            }
        }
    }
}
