package dev.muffar.moneyfikasi.common_ui.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.resource.R

@Composable
fun ColorPickerButton(
    modifier: Modifier = Modifier,
    color: Long,
    onClick: () -> Unit,
) {
    val containerColor = if (color == 0L) {
        MaterialTheme.colorScheme.secondaryContainer.copy(0.4f)
    } else {
        Color(color)
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(containerColor)
            .clickable { onClick() }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        if (color == 0L) {
            Icon(
                imageVector = Icons.Rounded.QuestionMark,
                contentDescription = stringResource(R.string.label_color),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
