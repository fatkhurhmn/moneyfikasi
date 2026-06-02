package dev.muffar.moneyfikasi.common_ui.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.icon.IconByName
import dev.muffar.moneyfikasi.resource.R

@Composable
fun IconFieldButton(
    modifier: Modifier = Modifier,
    icon: String,
    color: Long,
    showLabel: Boolean = true,
    onIconClick: () -> Unit,
) {
    val containerColor = if (color == 0L) {
        MaterialTheme.colorScheme.secondaryContainer.copy(0.5f)
    } else {
        Color(color).copy(alpha = 0.2f)
    }

    val iconColor = if (color == 0L) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        Color(color)
    }
    Column(
        modifier = modifier
    ) {
        if (showLabel) {
            Text(
                text = stringResource(R.string.label_icon),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(containerColor)
                .clickable { onIconClick() },
            contentAlignment = Alignment.Center
        ) {
            IconByName(
                name = icon,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
