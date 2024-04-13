package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import dev.muffar.moneyfikasi.resource.R

@Composable
fun TransactionsTopBar(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Text(
            text = stringResource(R.string.transaction_menu),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
            modifier = Modifier.align(Alignment.CenterStart)
        )
    }
}