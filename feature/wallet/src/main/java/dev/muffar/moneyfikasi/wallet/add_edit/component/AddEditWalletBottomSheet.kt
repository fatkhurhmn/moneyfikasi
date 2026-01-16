package dev.muffar.moneyfikasi.wallet.add_edit.component

import androidx.compose.runtime.Composable
import dev.muffar.moneyfikasi.common_ui.component.ColorPicker
import dev.muffar.moneyfikasi.common_ui.component.IconPicker
import dev.muffar.moneyfikasi.domain.utils.WalletIcon

@Composable
fun AddEditWalletBottomSheet(
    type: AddEditWalletBottomSheet,
    onIconSelect: (String) -> Unit,
    onColorSelect: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val icons = WalletIcon.getIcons()
    when (type) {
        AddEditWalletBottomSheet.ICON -> IconPicker(
            icons = icons,
            onClick = onIconSelect,
            onClose = onDismiss
        )

        AddEditWalletBottomSheet.COLOR -> ColorPicker(
            onClick = onColorSelect,
            onClose = onDismiss
        )
    }
}

enum class AddEditWalletBottomSheet {
    ICON,
    COLOR
}