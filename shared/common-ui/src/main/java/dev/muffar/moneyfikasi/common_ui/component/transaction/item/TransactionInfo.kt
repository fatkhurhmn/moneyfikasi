package dev.muffar.moneyfikasi.common_ui.component.transaction.item

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.icon.BoxedIcon
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.formattedDateTime
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.formattedTime

@Composable
fun TransactionInfo(
    transaction: Transaction,
    showDate: Boolean,
    modifier: Modifier = Modifier
) {
    val date = if (showDate) {
        transaction.date.formattedDateTime()
    } else {
        transaction.date.formattedTime()
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BoxedIcon(
            icon = transaction.category.icon,
            color = transaction.category.color
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = transaction.category.name,
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = date,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}