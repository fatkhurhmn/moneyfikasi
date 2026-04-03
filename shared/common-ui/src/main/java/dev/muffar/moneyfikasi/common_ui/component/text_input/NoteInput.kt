package dev.muffar.moneyfikasi.common_ui.component.text_input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import dev.muffar.moneyfikasi.resource.R

@Composable
fun NoteInput(
    note: String,
    onNoteChange: (String) -> Unit
) {
    CommonTextInput(
        modifier = Modifier.fillMaxWidth(),
        value = note,
        onValueChange = onNoteChange,
        label = stringResource(R.string.note),
        placeholder = stringResource(R.string.enter_note),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
        )
    )
}