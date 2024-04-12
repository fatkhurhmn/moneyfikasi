package dev.muffar.moneyfikasi.transaction.add_edit.component

import androidx.compose.runtime.Composable
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.Wallet

@Composable
fun AddEditTransactionBottomSheet(
    type: AddEditTransactionSheetType,
    categories: List<Category>,
    wallets: List<Wallet>,
    date: Long,
    hour: Int,
    minute: Int,
    onCategorySelect: (Category) -> Unit,
    onWalletSelect: (Wallet) -> Unit,
    onDateSelect: (Long) -> Unit,
    onTimeSelect: (Int, Int) -> Unit,
    onDismiss: () -> Unit,
) {
    when (type) {
        AddEditTransactionSheetType.CATEGORY -> CategoryPicker(
            categories = categories,
            onClick = onCategorySelect,
            onClose = onDismiss
        )

        AddEditTransactionSheetType.WALLET -> WalletPicker(
            wallets = wallets,
            onClick = onWalletSelect,
            onClose = onDismiss
        )

        AddEditTransactionSheetType.DATE -> DatePickerSheet(
            currentDate = date,
            onDateSelect = {
                onDateSelect(it)
                onDismiss()
            }
        )

        AddEditTransactionSheetType.TIME -> TimePickerSheet(
            minute = minute,
            hour = hour,
            onTimeSelect = { mHour, mMinute ->
                onTimeSelect(mHour, mMinute)
                onDismiss()
            }
        )
    }
}

enum class AddEditTransactionSheetType {
    DATE,
    TIME,
    CATEGORY,
    WALLET
}