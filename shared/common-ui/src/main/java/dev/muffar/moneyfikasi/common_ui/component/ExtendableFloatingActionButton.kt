package dev.muffar.moneyfikasi.common_ui.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.theme.color.MainColor
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.resource.R

@Composable
fun ExpandableFloatingActionButton(
    isExpanded: Boolean,
    onClick: () -> Unit,
    onTransactionClick: (TransactionType) -> Unit,
) {

    val fabSize = 58.dp
    val expandedFabWidth by animateDpAsState(
        targetValue = if (isExpanded) 150.dp else fabSize,
        animationSpec = spring(dampingRatio = 1f),
        label = ""
    )

    val expandedBoxHeight by animateDpAsState(
        targetValue = if (isExpanded) 140.dp else 0.dp,
        animationSpec = spring(dampingRatio = 1.5f),
        label = ""
    )

    Column(
        modifier = Modifier
    ) {
        Box(
            modifier = Modifier
                .offset(y = 25.dp)
                .size(
                    width = expandedFabWidth,
                    height = expandedBoxHeight
                )
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(18.dp)
                )
        ) {
            Column {
                CreateTransactionButton(
                    modifier = Modifier.clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp)),
                    text = TransactionType.INCOME.value,
                    icon = painterResource(R.drawable.ic_income),
                    onClick = { onTransactionClick(TransactionType.INCOME) }
                )
                CreateTransactionButton(
                    text = TransactionType.EXPENSE.value,
                    icon = painterResource(R.drawable.ic_expense),
                    onClick = { onTransactionClick(TransactionType.EXPENSE) }
                )
            }
        }

        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier
                .width(expandedFabWidth),
            shape = RoundedCornerShape(18.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_create),
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun CreateTransactionButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: Painter,
    onClick: () -> Unit,
) {
    val iconColor = if (text == TransactionType.INCOME.value) {
        MainColor.Green.primary
    } else {
        MainColor.Red.primary
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = icon,
            contentDescription = text,
            modifier = Modifier.size(24.dp),
            tint = iconColor
        )
    }
}