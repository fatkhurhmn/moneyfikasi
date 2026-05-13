package dev.muffar.moneyfikasi.common_ui.component.button

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.resource.R

@Composable
fun BottomBarAddEditButton(
    isEdit: Boolean,
    onSave: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .imePadding()
            .navigationBarsPadding()
    ) {
        CommonHorizontalDivider()
        if (!isEdit) {
            CommonButton(
                text = stringResource(R.string.save),
                onClick = onSave,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
            )
        } else {
            RowNegativePositiveButton(
                negativeText = stringResource(R.string.delete),
                positiveText = stringResource(R.string.save),
                onNegativeClick = onDelete,
                onPositiveClick = onSave,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}