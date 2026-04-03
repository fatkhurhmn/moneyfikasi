package dev.muffar.moneyfikasi.common_ui.component.transaction_item

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.IconByName
import dev.muffar.moneyfikasi.common_ui.theme.color.MainColor
import dev.muffar.moneyfikasi.domain.model.Category

@Composable
fun ItemCategoryIcon(category: Category?) {
    val color = if (category != null) Color(category.color) else MainColor.LightGray
    Card(
        colors = CardDefaults.cardColors(
            containerColor = color,
        ),
    ) {
        IconByName(
            name = category?.icon,
            tint = Color.White,
            modifier = Modifier
                .padding(8.dp)
                .size(26.dp)
        )
    }
}