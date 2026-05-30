package dev.muffar.moneyfikasi.common_ui.component.calendar_header

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.common_ui.component.icon.ArrowLeft
import dev.muffar.moneyfikasi.common_ui.component.icon.ArrowRight

@Composable
fun CalendarHeader(
    title: String,
    enableButton: Boolean = true,
    onPreviousClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        val arrowColor = if (enableButton) {
            MaterialTheme.colorScheme.onSurfaceVariant
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
                .background(MaterialTheme.colorScheme.primaryContainer),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onPreviousClick,
                enabled = enableButton
            ) {
                ArrowLeft(color = arrowColor)
            }

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 15.sp,
                modifier = Modifier.padding(4.dp)
            )

            IconButton(
                onClick = onNextClick,
                enabled = enableButton
            ) {
                ArrowRight(color = arrowColor)
            }
        }
    }
}
