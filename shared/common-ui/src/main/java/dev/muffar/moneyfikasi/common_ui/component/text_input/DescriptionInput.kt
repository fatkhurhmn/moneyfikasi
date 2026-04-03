package dev.muffar.moneyfikasi.common_ui.component.text_input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.resource.R

@Composable
fun DescriptionInput(
    description: String,
    onDescriptionChange: (String) -> Unit
) {
    CommonTextInput(
        modifier = Modifier.fillMaxWidth(),
        value = description,
        onValueChange = onDescriptionChange,
        label = stringResource(R.string.description),
        placeholder = stringResource(R.string.enter_description),
        onClear = { onDescriptionChange("") }
    )
}