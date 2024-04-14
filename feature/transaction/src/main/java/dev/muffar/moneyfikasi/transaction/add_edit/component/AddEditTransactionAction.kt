package dev.muffar.moneyfikasi.transaction.add_edit.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.resource.R

@Composable
fun AddEditTransactionAction(
    modifier: Modifier = Modifier,
    onSave: () -> Unit,
) {
    Row(
        modifier = modifier
    ) {
        Button(
            modifier = Modifier.weight(1f),
            shape = MaterialTheme.shapes.medium,
            onClick = onSave,
        ) {
            Icon(
                imageVector = Icons.Rounded.Save,
                contentDescription = stringResource(R.string.save)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(R.string.save))
        }
    }
}