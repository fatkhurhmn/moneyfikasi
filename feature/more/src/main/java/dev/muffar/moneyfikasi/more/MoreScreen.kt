package dev.muffar.moneyfikasi.more

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
    onSettingsClick: () -> Unit,
) {

    Scaffold(
        topBar = {
            MoreTopBar(onSettingsClick = onSettingsClick)
        }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier
                .padding(it)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                MoreItem(
                    label = stringResource(R.string.wallets),
                    title = stringResource(R.string.my_wallet),
                    description = stringResource(R.string.wallet_description),
                    status = stringResource(R.string.wallets_active, state.activeWalletsCount),
                    icon = painterResource(id = R.drawable.ic_wallet),
                    onClick = onWalletsClick
                )
            }
            item {
                MoreItem(
                    label = stringResource(R.string.categories),
                    title = stringResource(R.string.manage_categories),
                    description = stringResource(R.string.category_description),
                    status = stringResource(R.string.categories_count, state.categoriesCount),
                    icon = painterResource(id = R.drawable.ic_category),
                    onClick = onCategoriesClick
                )
            }
            item {
                MoreItem(
                    label = stringResource(R.string.presets),
                    title = stringResource(R.string.transaction_preset),
                    description = stringResource(R.string.preset_description),
                    status = stringResource(R.string.presets_saved, state.presetsCount),
                    icon = painterResource(id = R.drawable.ic_ink),
                    onClick = onPresetsClick
                )
            }
            item {
                MoreItem(
                    label = stringResource(R.string.budgets),
                    title = stringResource(R.string.financial_goals),
                    description = stringResource(R.string.financial_goals_description),
                    status = stringResource(R.string.budgets_set, state.budgetsCount),
                    icon = painterResource(id = R.drawable.ic_budget),
                    onClick = onBudgetsClick
                )
            }
        }
    }
}
