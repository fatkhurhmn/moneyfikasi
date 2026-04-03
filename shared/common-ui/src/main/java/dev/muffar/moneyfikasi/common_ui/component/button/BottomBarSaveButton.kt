package dev.muffar.moneyfikasi.common_ui.component.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.resource.R

@Composable
fun BottomBarSaveButton(onSave: () -> Unit) {
    BottomBarButton(
        title = stringResource(R.string.save),
        onClick = onSave
    )
}