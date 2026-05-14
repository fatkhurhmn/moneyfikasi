package dev.muffar.moneyfikasi.category.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.category.list.component.CategoriesContent
import dev.muffar.moneyfikasi.common_ui.component.EmptyDataList
import dev.muffar.moneyfikasi.common_ui.component.icon.BoxedIcon
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.button.CommonAddButton
import dev.muffar.moneyfikasi.common_ui.component.tabs.IncomeExpenseTabs
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.domain.model.Category
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
                containerColor = MaterialTheme.colorScheme.background
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
                    categoryType = CategoryType.INCOME,
                    onClick = onCategoryItemClick
                )

                1 -> CategoriesContent(
                    categories = expenseCategories,
                    categoryType = CategoryType.EXPENSE,
                    onClick = onCategoryItemClick
                )
            }
        }
    }
}
