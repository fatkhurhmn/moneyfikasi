package dev.muffar.moneyfikasi.transaction.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.formattedDateTime
import dev.muffar.moneyfikasi.utils.extensions.LocalDateTimeExt.formattedTime
import org.threeten.bp.LocalDateTime

@Composable
fun TransactionDetailBody(
    modifier: Modifier = Modifier,
    date: LocalDateTime?,
    wallet: Wallet?,
    category: Category?,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        RowDetailBody(
            label = stringResource(R.string.label_date),
            value = date?.formattedDateTime() ?: ""
        )

        RowDetailBody(
            label = stringResource(R.string.label_time),
            value = date?.formattedTime() ?: ""
        )

        if (category != null) {
            RowDetailBodyWithIcon(
                label = stringResource(R.string.label_category),
                icon = category.icon,
                value = category.name,
                color = category.color
            )
        }

        if (wallet != null) {
            RowDetailBodyWithIcon(
                label = stringResource(R.string.label_wallet),
                icon = wallet.icon,
                value = wallet.name,
                color = wallet.color
            )
        }
    }
}