package dev.muffar.moneyfikasi.preset.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonAddButton
import dev.muffar.moneyfikasi.common_ui.component.tabs.IncomeExpenseTabs
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.preset.list.component.PresetsContent
import dev.muffar.moneyfikasi.resource.R
import java.util.UUID

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PresetsScreen(
    modifier: Modifier = Modifier,
    state: PresetsState,
    onAddPresetClick: (TransactionType) -> Unit,
    onPresetClick: (TransactionType, UUID) -> Unit,
    onBackClick: () -> Unit,
) {
    val incomePresets = state.presets.filter { it.type == TransactionType.INCOME }
    val expensePresets = state.presets.filter { it.type == TransactionType.EXPENSE }
    val pagerState = rememberPagerState { state.tabs.size }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.presets),
                onBackClick = onBackClick
            )
        },
        floatingActionButton = {
            CommonAddButton(
                onClick = {
                    val currentTab = pagerState.currentPage
                    onAddPresetClick(TransactionType.valueOf(state.tabs[currentTab]))
                }
            )
        }
    ) { paddingValues ->
        IncomeExpenseTabs(
            modifier = modifier.padding(paddingValues),
            pagerState = pagerState
        ) { index ->
            when (index) {
                0 -> PresetsContent(
                    presets = incomePresets,
                    onClick = { onPresetClick(TransactionType.INCOME, it) }
                )

                1 -> PresetsContent(
                    presets = expensePresets,
                    onClick = { onPresetClick(TransactionType.EXPENSE, it) }
                )
            }
        }
    }
}
