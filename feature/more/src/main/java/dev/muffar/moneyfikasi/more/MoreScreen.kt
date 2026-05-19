package dev.muffar.moneyfikasi.more

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BookmarkAdded
import androidx.compose.material.icons.rounded.DataUsage
import androidx.compose.material.icons.rounded.Insights
import androidx.compose.material.icons.rounded.PostAdd
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material.icons.rounded.Wallet
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
            modifier = modifier
                .padding(it)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalItemSpacing = 8.dp
        ) {
            item {
                MoreItem(
                    label = stringResource(R.string.wallets),
                    title = stringResource(R.string.my_wallet),
                    description = stringResource(R.string.wallet_description),
                    status = stringResource(R.string.wallets_active, state.activeWalletsCount),
                    icon = Icons.Rounded.Wallet,
                    onClick = onWalletsClick
                )
            }
            item {
                MoreItem(
                    label = stringResource(R.string.categories),
                    title = stringResource(R.string.manage_categories),
                    description = stringResource(R.string.category_description),
                    status = stringResource(R.string.categories_count, state.categoriesCount),
                    icon = Icons.Rounded.Widgets,
                    onClick = onCategoriesClick
                )
            }
            item {
                MoreItem(
                    label = stringResource(R.string.presets),
                    title = stringResource(R.string.transaction_preset),
                    description = stringResource(R.string.preset_description),
                    status = stringResource(R.string.presets_saved, state.presetsCount),
                    icon = Icons.Rounded.BookmarkAdded,
                    onClick = onPresetsClick
                )
            }
            item {
                MoreItem(
                    label = stringResource(R.string.budgets),
                    title = stringResource(R.string.financial_goals),
                    description = stringResource(R.string.financial_goals_description),
                    status = stringResource(R.string.budgets_set, state.budgetsCount),
                    icon = Icons.Rounded.DataUsage,
                    onClick = onBudgetsClick
                )
            }
            item {
                MoreItem(
                    label = stringResource(R.string.recurring_transactions),
                    title = stringResource(R.string.scheduled_transactions),
                    description = stringResource(R.string.recurring_transactions_description),
                    status = stringResource(R.string.recurring_transactions_active, state.recurringTransactionsCount),
                    icon = Icons.Rounded.TrackChanges,
                    onClick = onRecurringTransactionsClick
                )
            }
        }
    }
}
