package dev.muffar.moneyfikasi.feature.dashboard.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.theme.color.MainColor
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.capitalize
import dev.muffar.moneyfikasi.utils.extensions.formatThousand

@Composable
fun OverviewIncomeExpense(
    modifier: Modifier = Modifier,
    categoryType: CategoryType,
    amount: Double
) {
    val color = when (categoryType) {
        CategoryType.INCOME -> MainColor.Green.extraLight
        CategoryType.EXPENSE -> MainColor.Red.extraLight
    }
    val icon = when (categoryType) {
        CategoryType.INCOME -> R.drawable.ic_income
        CategoryType.EXPENSE -> R.drawable.ic_expense
    }

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(color.copy(alpha = 0.7f))
    ) {
        Image(
            modifier = Modifier
                .size(50.dp)
                .padding(8.dp)
                .align(Alignment.CenterStart),
            painter = painterResource(id = icon),
            contentDescription = null,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = categoryType.name.capitalize(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = amount.formatThousand(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}