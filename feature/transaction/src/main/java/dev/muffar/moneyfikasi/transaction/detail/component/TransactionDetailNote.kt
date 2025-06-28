package dev.muffar.moneyfikasi.transaction.detail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.muffar.moneyfikasi.resource.R

@Composable
fun TransactionDetailNote(note: String) {
    Column {
        Text(
            text = stringResource(R.string.note),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = note,
            style = MaterialTheme.typography.labelLarge.copy(
                lineHeight = 20.sp,
            )
        )
    }
}