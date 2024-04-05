package dev.muffar.moneyfikasi.category.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.category.list.component.CategoriesContent
import dev.muffar.moneyfikasi.common_ui.component.CommonAddButton
import dev.muffar.moneyfikasi.common_ui.component.CommonTabs
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.resource.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    state: CategoriesState,
    onAddCategoryClick: (CategoryType) -> Unit,
    onBackClick: () -> Unit,
) {
    val expenseCategories = state.categories.filter { it.type == CategoryType.EXPENSE }
    val incomeCategories = state.categories.filter { it.type == CategoryType.INCOME }

    val pagerState = rememberPagerState { state.tabs.size }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.categories),
                onBackClick = onBackClick
            )
        },
        floatingActionButton = {
            CommonAddButton(
                onClick = {
                    val currentTab = pagerState.currentPage
                    onAddCategoryClick(CategoryType.entries[currentTab])
                }
            )
        }
    ) {
        CommonTabs(
            modifier = modifier.padding(it),
            tabs = state.tabs,
            pagerState = pagerState
        ) { index ->
            when (index) {
                0 -> CategoriesContent(categories = expenseCategories)
                1 -> CategoriesContent(categories = incomeCategories)
            }
        }
    }
}