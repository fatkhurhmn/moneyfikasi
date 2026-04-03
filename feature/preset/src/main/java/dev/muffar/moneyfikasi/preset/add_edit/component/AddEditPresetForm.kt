package dev.muffar.moneyfikasi.preset.add_edit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.text_input.AmountInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.CategoryInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.CommonTextInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.DescriptionInput
import dev.muffar.moneyfikasi.common_ui.component.text_input.WalletInput
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.domain.model.Wallet
import dev.muffar.moneyfikasi.preset.add_edit.AddEditPresetState
import dev.muffar.moneyfikasi.resource.R

@Composable
fun AddEditPresetForm(
    modifier: Modifier = Modifier,
    state: AddEditPresetState,
    onNameChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onCategoryChange: (Category?) -> Unit,
    onAddNewCategoryClick: () -> Unit,
    onWalletChange: (Wallet?) -> Unit,
    onAddNewWalletClick: () -> Unit,
    onDescriptionChange: (String) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CommonTextInput(
            value = state.name,
            onValueChange = onNameChange,
            label = stringResource(R.string.preset_name),
            placeholder = stringResource(R.string.enter_preset_name),
            error = state.nameError
        )

        DescriptionInput(
            description = state.description,
            onDescriptionChange = onDescriptionChange
        )

        AmountInput(
            amount = state.amount,
            onAmountChange = onAmountChange,
            onClear = { onAmountChange("0") }
        )

        CategoryInput(
            category = state.category ?: Category(),
            categoryOptions = state.categories,
            onCategorySelect = onCategoryChange,
            onAddNewCategoryClick = onAddNewCategoryClick,
            onClear = { onCategoryChange(null) }
        )

        WalletInput(
            wallet = state.wallet ?: Wallet(),
            walletOptions = state.wallets,
            onWalletSelect = onWalletChange,
            onAddNewWalletClick = onAddNewWalletClick,
            onClear = { onWalletChange(null) }
        )
    }
}
