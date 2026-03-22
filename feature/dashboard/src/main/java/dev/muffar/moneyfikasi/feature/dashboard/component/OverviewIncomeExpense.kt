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
import androidx.compose.ui.draw.alpha
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
    amount: Double,
) {
    val isIncome = categoryType == CategoryType.INCOME
    val containerColor = if (isIncome) MainColor.Green.extraLight else MainColor.Red.extraLight
    val textColor = if (isIncome) MainColor.Green.primary else MainColor.Red.primary
    val icon = if (isIncome) R.drawable.ic_income else R.drawable.ic_expense

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(containerColor.copy(alpha = 0.7f))
    ) {
        Image(
            modifier = Modifier
                .size(50.dp)
                .padding(8.dp)
                .align(Alignment.TopEnd)
                .alpha(0.5f),
            painter = painterResource(id = icon),
            contentDescription = null,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = categoryType.name.lowercase().capitalize(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = amount.formatThousand(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.Center,
            )
        }
    }
}