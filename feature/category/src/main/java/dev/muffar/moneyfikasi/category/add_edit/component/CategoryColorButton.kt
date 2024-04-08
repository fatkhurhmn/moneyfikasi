package dev.muffar.moneyfikasi.category.add_edit.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.ColorPickerButton
import dev.muffar.moneyfikasi.resource.R

@Composable
fun CategoryColorButton(
    modifier: Modifier = Modifier,
    color: Long,
    onColorClick: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.color),
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(modifier = Modifier.height(4.dp))
        ColorPickerButton(
            color = color,
            onClick = onColorClick
        )
    }
}