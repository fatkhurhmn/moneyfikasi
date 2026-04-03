package dev.muffar.moneyfikasi.preset.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.CommonTabs
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.button.CommonAddButton
import dev.muffar.moneyfikasi.common_ui.theme.color.MainColor
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.preset.list.component.PresetsContent
import dev.muffar.moneyfikasi.resource.R
import java.util.UUID

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PresetListScreen(
    modifier: Modifier = Modifier,
    state: PresetListState,
    onAddPresetClick: (TransactionType) -> Unit,
    onPresetClick: (TransactionType, UUID) -> Unit,
    onBackClick: () -> Unit,
) {
    val incomePresets = state.presets.filter { it.type == TransactionType.INCOME }
    val expensePresets = state.presets.filter { it.type == TransactionType.EXPENSE }

    val tabs = mapOf(
        TransactionType.INCOME.name to MainColor.Green.primary,
        TransactionType.EXPENSE.name to MainColor.Red.primary,
    )
    val pagerState = rememberPagerState { state.tabs.size }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.preset),
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
        CommonTabs(
            modifier = modifier.padding(paddingValues),
            tabs = tabs,
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
