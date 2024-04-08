package dev.muffar.moneyfikasi.category.add_edit.component

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
fun ColorPicker(
    modifier: Modifier = Modifier,
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
            .height(50.dp)
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        if (color == 0L) {
            Icon(
                imageVector = Icons.Rounded.QuestionMark,
                contentDescription = stringResource(R.string.color),
                tint = MaterialTheme.colorScheme.outline.copy(0.8f)
            )
        }
    }
}
