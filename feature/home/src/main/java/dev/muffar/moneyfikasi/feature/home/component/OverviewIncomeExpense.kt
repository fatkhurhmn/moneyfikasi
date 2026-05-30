package dev.muffar.moneyfikasi.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowDown
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand

@Composable
fun OverviewIncomeExpense(
    modifier: Modifier = Modifier,
    categoryType: CategoryType,
    amount: Double,
    isAmountVisible: Boolean,
) {
    val isIncome = categoryType == CategoryType.INCOME
    val financeColors = MoneyfikasiTheme.financeColors
    val containerColor =
        if (isIncome) financeColors.incomeContainer else financeColors.expenseContainer
    val textColor = if (isIncome) financeColors.income else financeColors.expense
    val icon =
        if (isIncome) Icons.Rounded.KeyboardDoubleArrowDown else Icons.Rounded.KeyboardDoubleArrowUp
    val label =
        if (isIncome) R.string.label_income else R.string.label_expense

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(containerColor)
    ) {
        Icon(
            modifier = Modifier
                .size(50.dp)
                .padding(8.dp)
                .align(Alignment.TopEnd),
            imageVector = icon,
            contentDescription = null,
            tint = textColor
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(label),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            val amountValue =
                if (isAmountVisible) amount.formatThousand()
                else stringResource(R.string.label_invisible_balance)

            Text(
                text = amountValue,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.Center,
            )
        }
    }
}