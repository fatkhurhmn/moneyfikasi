package dev.muffar.moneyfikasi.common_ui.component.top_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun TopBarButton(
    painter: Painter,
    onClick: () -> Unit,
) {
    IconButton(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .size(40.dp)
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
}
