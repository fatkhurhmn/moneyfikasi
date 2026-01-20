package dev.muffar.moneyfikasi.transaction.transfer.component

import androidx.compose.runtime.Composable
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.DatePickerSheetV1
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.TimePickerSheetV1
import dev.muffar.moneyfikasi.common_ui.component.text_input.WalletPicker
import dev.muffar.moneyfikasi.domain.model.Wallet

@Composable
fun AddEditTransactionBottomSheet(
    type: TransferTransactionSheetType,
    date: Long,
    hour: Int,
    minute: Int,
    wallets: List<Wallet>,
    onSourceWalletSelect: (Wallet) -> Unit,
    onTargetWalletSelect: (Wallet) -> Unit,
    onDateSelect: (Long) -> Unit,
    onTimeSelect: (Int, Int) -> Unit,
    onDismiss: () -> Unit,
    onAddWallet: () -> Unit,
) {
    when (type) {
        TransferTransactionSheetType.SOURCE_WALLET -> WalletPicker(
            wallets = wallets,
            onClick = onSourceWalletSelect,
            onAdd = onAddWallet,
            onClose = onDismiss
        )

        TransferTransactionSheetType.TARGET_WALLET -> WalletPicker(
            wallets = wallets,
            onClick = onTargetWalletSelect,
            onAdd = onAddWallet,
            onClose = onDismiss
        )

        TransferTransactionSheetType.DATE -> DatePickerSheetV1(
            currentDate = date,
            onDateSelect = {
                onDateSelect(it)
                onDismiss()
            }
        )

        TransferTransactionSheetType.TIME -> TimePickerSheetV1(
            minute = minute,
            hour = hour,
            onTimeSelect = { mHour, mMinute ->
                onTimeSelect(mHour, mMinute)
                onDismiss()
            }
        )
    }
}

enum class TransferTransactionSheetType {
    DATE,
    TIME,
    SOURCE_WALLET,
    TARGET_WALLET
}