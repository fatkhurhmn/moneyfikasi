package dev.muffar.moneyfikasi.common_ui.component.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.theme.color.MainColor
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.utils.extensions.capitalize

@Composable
fun IncomeExpenseTabHeader(
    modifier: Modifier = Modifier,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabs = mapOf(
        CategoryType.INCOME.name to MainColor.Green.primary,
        CategoryType.EXPENSE.name to MainColor.Red.primary,
    )

    var border by remember { mutableStateOf(MainColor.White) }

    LaunchedEffect(selectedTab) {
        border = tabs.values.elementAt(selectedTab)
    }

    Box(
        modifier = modifier
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
                        .clickable { onTabSelected(index) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title.capitalize(),
                        color = textColor,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}