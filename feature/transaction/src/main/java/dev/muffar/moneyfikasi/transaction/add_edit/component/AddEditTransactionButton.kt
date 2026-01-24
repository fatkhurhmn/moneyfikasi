package dev.muffar.moneyfikasi.transaction.add_edit.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.button.CommonButton
import dev.muffar.moneyfikasi.resource.R

@Composable
fun AddEditTransactionButton(
    onSave: () -> Unit,
) {
    Column(
        modifier = Modifier.imePadding()
    ) {
        CommonHorizontalDivider()
        CommonButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            text = stringResource(R.string.save),
            onClick = onSave
        )
    }
}