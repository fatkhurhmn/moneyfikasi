package dev.muffar.moneyfikasi.common_ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.theme.color.MainColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CommonTabs(
    modifier: Modifier = Modifier,
    tabs: Map<String, Color>,
    pagerState: PagerState,
    horizontalPager: @Composable (Int) -> Unit,
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var border by remember { mutableStateOf(MainColor.White) }

    LaunchedEffect(selectedTab) {
        pagerState.animateScrollToPage(selectedTab)
    }

    LaunchedEffect(pagerState.currentPage) {
        selectedTab = pagerState.currentPage
        border = tabs.values.elementAt(selectedTab)
    }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .clip(CircleShape)
                .border(0.8f.dp, border, CircleShape)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.entries.forEachIndexed { index, (title, color) ->
                    val selected = selectedTab == index

                    val bgColor = if (selected) color else MaterialTheme.colorScheme.surface

                    val textColor =
                        if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(bgColor)
                            .clickable { selectedTab = index }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            color = textColor,
                            style = MaterialTheme.typography.labelMedium
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
            Box(modifier = Modifier.fillMaxSize()) {
                horizontalPager(it)
            }
        }
    }
}