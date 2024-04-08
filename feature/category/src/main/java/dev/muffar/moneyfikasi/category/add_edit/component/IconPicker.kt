package dev.muffar.moneyfikasi.category.add_edit.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import dev.muffar.moneyfikasi.common_ui.component.IconByName
import dev.muffar.moneyfikasi.resource.R

@Composable
fun IconPicker(
    modifier: Modifier = Modifier,
    icon: String,
    color: Long,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .background(
                if (color == 0L) {
                    MaterialTheme.colorScheme.surfaceVariant
                } else {
                    Color(color)
                }
            )
            .size(50.dp)
            .clickable { onClick() }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        if (icon.isEmpty()) {
            Icon(
                imageVector = Icons.Rounded.QuestionMark,
                contentDescription = stringResource(R.string.icon),
                tint = if (color == 0L) {
                    MaterialTheme.colorScheme.outline.copy(0.8f)
                } else {
                    MaterialTheme.colorScheme.background
                }
            )
        } else {
            IconByName(
                name = icon,
                tint = if (color == 0L) {
                    MaterialTheme.colorScheme.outline.copy(0.8f)
                } else {
                    MaterialTheme.colorScheme.background
                }
            )
        }
    }
}