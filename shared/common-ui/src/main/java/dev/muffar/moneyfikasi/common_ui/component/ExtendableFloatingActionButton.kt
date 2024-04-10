package dev.muffar.moneyfikasi.common_ui.component

import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.SyncAlt
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.resource.R

@Composable
fun ExpandableFloatingActionButton(
    isExpanded : Boolean,
    fabIcon: ImageVector,
    onClick: () -> Unit,
    onTransactionClick: (TransactionType) -> Unit,
) {

    val fabSize = 58.dp
    val expandedFabWidth by animateDpAsState(
        targetValue = if (isExpanded) 200.dp else fabSize,
        animationSpec = spring(dampingRatio = 3f),
        label = ""
    )

    val expandedBoxHeight by animateDpAsState(
        targetValue = if (isExpanded) 192.dp else 0.dp,
        animationSpec = spring(dampingRatio = 4f),
        label = ""
    )

    val expandedXOffsetIcon by animateDpAsState(
        targetValue = if (isExpanded) (-70).dp else 0.dp,
        animationSpec = spring(dampingRatio = 3f),
        label = ""
    )

    val expandedXOffsetText by animateDpAsState(
        targetValue = if (isExpanded) 10.dp else 50.dp,
        animationSpec = spring(dampingRatio = 3f),
        label = ""
    )

    val expandedAlphaText by animateFloatAsState(
        targetValue = if (isExpanded) 1f else 0f,
        animationSpec = tween(
            durationMillis = if (isExpanded) 350 else 100,
            delayMillis = if (isExpanded) 100 else 0,
            easing = EaseIn
        ), label = ""
    )

    Column (
        modifier=Modifier
    ){
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
                    icon = Icons.Rounded.ArrowDownward,
                    onClick = { onTransactionClick(TransactionType.INCOME) }
                )
                CreateTransactionButton(
                    text = TransactionType.EXPENSE.value,
                    icon = Icons.Rounded.ArrowUpward,
                    onClick = { onTransactionClick(TransactionType.EXPENSE) }
                )
                CreateTransactionButton(
                    text = TransactionType.TRANSFER.value,
                    icon = Icons.Rounded.SyncAlt,
                    onClick = { onTransactionClick(TransactionType.TRANSFER) }
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
                imageVector = fabIcon,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .offset(x = expandedXOffsetIcon)
            )

            Text(
                text = stringResource(R.string.create_transaction),
                softWrap = false,
                modifier = Modifier
                    .offset(x = expandedXOffsetText)
                    .alpha(expandedAlphaText)
            )
        }
    }
}

@Composable
fun CreateTransactionButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            modifier = Modifier.weight(1f)
        )
    }
}