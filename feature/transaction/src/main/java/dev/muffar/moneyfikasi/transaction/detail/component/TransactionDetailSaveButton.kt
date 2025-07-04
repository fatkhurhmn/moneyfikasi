package dev.muffar.moneyfikasi.transaction.detail.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
fun TransactionDetailSaveButton(
    onClick: () -> Unit
) {
    Button(
        onClick,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.save_to_gallery),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_download),
            contentDescription = stringResource(R.string.save),
            modifier = Modifier.size(24.dp),
        )
    }
}