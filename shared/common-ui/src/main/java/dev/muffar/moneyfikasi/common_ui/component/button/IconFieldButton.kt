package dev.muffar.moneyfikasi.common_ui.component.button

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.icon.BoxedIcon
import dev.muffar.moneyfikasi.resource.R

@Composable
fun IconFieldButton(
    modifier: Modifier = Modifier,
    icon: String,
    color: Long,
    showLabel: Boolean = true,
    onIconClick: () -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = if (showLabel) stringResource(R.string.icon) else "",
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(modifier = Modifier.height(4.dp))
        BoxedIcon(
            icon = icon,
            color = color,
            containerSize = 50.dp,
            onClick = onIconClick
        )
    }
}