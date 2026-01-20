package dev.muffar.moneyfikasi.transaction.detail.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.domain.model.TransferDetail

@Composable
fun TransferDetailCard(
    transferDetail: TransferDetail
) {
    TransactionDetailHeader()
    Spacer(modifier = Modifier.height(32.dp))
    TransactionDetailTransfer(
        amount = transferDetail.amount,
        sourceWallet = transferDetail.sourceWallet,
        targetWallet = transferDetail.targetWallet
    )
    TransactionDetailDivider()
    TransactionDetailBody(
        date = transferDetail.date,
        wallet = null,
        category = null,
    )
    if (transferDetail.fee > 0) {
        TransactionDetailDivider()
        TransactionDetailAdmin(transferDetail.fee)
    }
}