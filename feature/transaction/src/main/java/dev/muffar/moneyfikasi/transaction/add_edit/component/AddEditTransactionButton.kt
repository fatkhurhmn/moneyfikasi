package dev.muffar.moneyfikasi.transaction.add_edit.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.resource.R

@Composable
fun AddEditTransactionButton(
    onCreate: () -> Unit,
) {
    Button(
        onClick = onCreate,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.create),
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_create_transaction),
            contentDescription = stringResource(R.string.create),
            modifier = Modifier.size(24.dp),
        )
    }
}