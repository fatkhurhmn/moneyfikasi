package dev.muffar.moneyfikasi.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.IconByName
import dev.muffar.moneyfikasi.common_ui.theme.color.MainColor
import dev.muffar.moneyfikasi.domain.model.Preset

@Composable
fun QuickTransactionItem(
    modifier: Modifier = Modifier,
    preset: Preset,
    itemWidth: Dp,
    aspectRatio: Float = 1f,
    onClick: () -> Unit,
) {
    val color = preset.category?.color
    Box(
        modifier = modifier
            .background(
                if (color == null) MainColor.LightGray else Color(color),
                MaterialTheme.shapes.large
            )
            .width(itemWidth)
            .aspectRatio(aspectRatio)
            .clip(MaterialTheme.shapes.large)
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            IconByName(
                name = preset.category?.icon,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = preset.name,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                color = Color.White,
                lineHeight = 12.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}