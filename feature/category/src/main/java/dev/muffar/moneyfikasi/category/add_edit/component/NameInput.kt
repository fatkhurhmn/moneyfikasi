package dev.muffar.moneyfikasi.category.add_edit.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import dev.muffar.moneyfikasi.common_ui.component.text_input.CommonTextInput
import dev.muffar.moneyfikasi.resource.R

@Composable
fun NameInput(
    name: String,
    onNameChange: (String) -> Unit,
) {
    CommonTextInput(
        modifier = Modifier.fillMaxWidth(),
        value = name,
        onValueChange = onNameChange,
        label = stringResource(R.string.name),
        placeholder = stringResource(R.string.enter_category_name),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        )
    )
}