package dev.muffar.moneyfikasi.wallet.add_edit.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import dev.muffar.moneyfikasi.common_ui.component.text_input.CommonTextInput
import dev.muffar.moneyfikasi.domain.model.ErrorMessage
import dev.muffar.moneyfikasi.resource.R

@Composable
fun WalletNameInput(
    name: String,
    onNameChange: (String) -> Unit,
    error: ErrorMessage
) {
    CommonTextInput(
        modifier = Modifier.fillMaxWidth(),
        value = name,
        onValueChange = onNameChange,
        label = stringResource(R.string.label_name),
        error = error,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        )
    )
}