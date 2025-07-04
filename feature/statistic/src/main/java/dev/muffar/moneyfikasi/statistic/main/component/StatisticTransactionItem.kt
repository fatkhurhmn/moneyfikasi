package dev.muffar.moneyfikasi.statistic.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.IconByName
import dev.muffar.moneyfikasi.common_ui.theme.color.MainColor
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.format
import dev.muffar.moneyfikasi.utils.formatThousand

@Composable
fun StatisticTransactionItem(
    modifier: Modifier = Modifier,
    category: Category,
    amount: Double,
    percentage: Double,
    quantity: Int,
) {
    val formattedAmount = amount.toLong().formatThousand().let {
        if (category.type == CategoryType.INCOME) "+$it" else "-$it"
    }

    val color =
        if (category.type == CategoryType.INCOME) MainColor.Green.primary else MainColor.Red.primary

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .background(Color(category.color))
                    .padding(6.dp),
                contentAlignment = Alignment.Center
            ) {
                IconByName(name = category.icon, tint = MainColor.White)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp)
                )
                Text(
                    text = "${(percentage * 100).format(2)}%",
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = formattedAmount,
                color = color,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = stringResource(R.string.qty_transactions, quantity),
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}