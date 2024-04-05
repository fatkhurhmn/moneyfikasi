package dev.muffar.moneyfikasi.common_ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommonTabs(
    modifier: Modifier = Modifier,
    tabs: List<String>,
    horizontalPager : @Composable (Int) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val pagerState = rememberPagerState { tabs.size }

    LaunchedEffect(selectedTab) {
        pagerState.animateScrollToPage(selectedTab)
    }

    LaunchedEffect(pagerState.currentPage) {
        selectedTab = pagerState.currentPage
    }

    Column(
        modifier = modifier
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            divider = {},
        ) {
            tabs.forEachIndexed { index, title ->
                val selected = selectedTab == index

                val titleColor = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline
                }

                val titleWeight = if (selected) {
                    FontWeight.Medium
                } else {
                    FontWeight.Normal
                }

                Tab(
                    selected = selected,
                    onClick = { selectedTab = index },
                ) {
                    Box(
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = title,
                            color = titleColor,
                            fontWeight = titleWeight
                        )
                    }
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                horizontalPager(it)
            }
        }
    }
}