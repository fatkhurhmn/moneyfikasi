package dev.muffar.moneyfikasi.feature.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun VisibilityButton(
    modifier: Modifier = Modifier,
    visibility: Boolean,
    color: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    onVisibilityClick: () -> Unit
) {
    Icon(
        imageVector = if (visibility) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
        contentDescription = null,
        tint = color,
        modifier = modifier
            .size(20.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onVisibilityClick
            )
    )
}