package dev.muffar.moneyfikasi.common_ui.component.tabs

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class DebtTab(
    @param:StringRes val labelResId: Int,
    val color: Color
)

@Composable
fun DebtTypeTabHeader(
    modifier: Modifier = Modifier,
    labelStyle: TextStyle = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp),
    innerPadding: PaddingValues = PaddingValues(vertical = 6.dp),
    tabs: List<DebtTab>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    var border by remember { mutableStateOf(Color.White) }

    LaunchedEffect(selectedTab) {
        border = tabs[selectedTab].color
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .border(1.dp, border, CircleShape)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            tabs.forEachIndexed { index, (title, color) ->
                val selected = selectedTab == index
                val bgColor = if (selected) color else Color.Transparent
                val textColor =
                    if (selected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface
                Box(
                    modifier = Modifier
                        .padding(3.dp)
                        .weight(1f)
                        .clip(CircleShape)
                        .background(bgColor)
                        .clickable { onTabSelected(index) }
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(title),
                        color = textColor,
                        style = labelStyle
                    )
                }
            }
        }
    }
}
