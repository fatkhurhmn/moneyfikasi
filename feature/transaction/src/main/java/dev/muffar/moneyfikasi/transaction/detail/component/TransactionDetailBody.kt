package dev.muffar.moneyfikasi.transaction.detail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.resource.R

@Composable
fun TransactionDetailBody(
    modifier: Modifier = Modifier,
    wallet: String,
    category: String,
    type: String,
    dateTime: String,
    description: String,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        RowDetailBody(title = stringResource(R.string.wallet), value = wallet)
        RowDetailBody(title = stringResource(R.string.category), value = category)
        RowDetailBody(title = stringResource(R.string.type), value = type)
        RowDetailBody(title = stringResource(R.string.date), value = dateTime)
        RowDetailBody(
            title = stringResource(R.string.description),
            value = description.ifEmpty { "-" }
        )
    }
}