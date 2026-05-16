package dev.muffar.moneyfikasi.common_ui.component.container

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PrimaryCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    shape: CornerBasedShape = MaterialTheme.shapes.medium,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = shape,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        content()
    }
}