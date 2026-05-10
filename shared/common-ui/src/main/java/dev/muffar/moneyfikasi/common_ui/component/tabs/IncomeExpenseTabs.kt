package dev.muffar.moneyfikasi.common_ui.component.tabs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IncomeExpenseTabs(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    tabPadding: PaddingValues = PaddingValues(16.dp),
    fillMaxSize: Boolean = true,
    horizontalPager: @Composable (Int) -> Unit,
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    LaunchedEffect(selectedTab) {
        pagerState.animateScrollToPage(selectedTab)
    }

    LaunchedEffect(pagerState.currentPage) {
        selectedTab = pagerState.currentPage
    }

    Column(modifier = modifier) {
        IncomeExpenseTabHeader(
            modifier = Modifier.padding(tabPadding),
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .then(if (fillMaxSize) Modifier.weight(1f) else Modifier)
        ) {
            Box(modifier = Modifier.then(if (fillMaxSize) Modifier.fillMaxSize() else Modifier)) {
                horizontalPager(it)
            }
        }
    }
}