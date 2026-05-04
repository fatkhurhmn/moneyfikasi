package dev.muffar.moneyfikasi.common_ui.component.icon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun LabeledIcon(
    icon: String,
    label: String,
    color: Long?,
    modifier: Modifier = Modifier,
    isLabelPrefix: Boolean = true,
    fill: Boolean = false
) {
    val containerColor = if (color == 0L || color == null) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        Color(color).copy(alpha = 0.2f)
    }

    val iconColor = if (color == 0L || color == null) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        Color(color)
    }

    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(containerColor)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (isLabelPrefix) Arrangement.End else Arrangement.Start
    ) {
        if (isLabelPrefix) {
            LabelText(
                label = label,
                color = iconColor,
                modifier = Modifier
                    .then(if (fill) Modifier.fillMaxWidth() else Modifier)
                    .weight(1f, fill = false)
                    .padding(end = 8.dp)
            )
        }

        IconByName(
            name = icon,
            tint = iconColor,
            modifier = Modifier.size(20.dp)
        )

        if (!isLabelPrefix) {
            LabelText(
                label = label,
                color = iconColor,
                modifier = Modifier
                    .then(if (fill) Modifier.fillMaxWidth() else Modifier)
                    .weight(1f, fill = false)
                    .padding(start = 8.dp)
            )
        }
    }
}

@Composable
private fun LabelText(
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        color = color,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}