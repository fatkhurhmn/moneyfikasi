package dev.muffar.moneyfikasi.category.add_edit.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.resource.R

@Composable
fun AddEditCategoryAction(
    modifier: Modifier = Modifier,
    isEdit: Boolean,
    onSave: () -> Unit,
    onDelete: () -> Unit,
) {
    Row(
        modifier = modifier
    ) {
        if (isEdit) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                onClick = onDelete,
            ) {
                Text(text = stringResource(R.string.delete))
            }
            Spacer(modifier = Modifier.width(16.dp))
        }

        Button(
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            shape = MaterialTheme.shapes.large,
            onClick = onSave,
        ) {
            Text(text = stringResource(R.string.save))
        }
    }
}