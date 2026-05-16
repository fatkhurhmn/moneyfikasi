package dev.muffar.moneyfikasi.category.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.category.list.component.CategoriesContent
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.button.CommonAddButton
import dev.muffar.moneyfikasi.common_ui.component.tabs.IncomeExpenseTabs
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.resource.R
import java.util.UUID

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    state: CategoriesState,
    onAddCategoryClick: (CategoryType) -> Unit,
    onCategoryItemClick: (CategoryType, UUID) -> Unit,
    onBackClick: () -> Unit,
) {
    val expenseCategories = state.categories.filter { it.isExpense }
    val incomeCategories = state.categories.filter { it.isIncome }
    val pagerState = rememberPagerState { state.tabs.size }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.categories),
                onBackClick = onBackClick,
            )
        },
        floatingActionButton = {
            CommonAddButton(
                onClick = {
                    val currentTab = pagerState.currentPage
                    onAddCategoryClick(CategoryType.fromString(state.tabs[currentTab]))
                }
            )
        }
    ) {
        IncomeExpenseTabs(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(it),
            pagerState = pagerState
        ) { index ->
            when (index) {
                0 -> CategoriesContent(
                    categories = incomeCategories,
                    onClick = onCategoryItemClick
                )

                1 -> CategoriesContent(
                    categories = expenseCategories,
                    onClick = onCategoryItemClick
                )
            }
        }
    }
}
