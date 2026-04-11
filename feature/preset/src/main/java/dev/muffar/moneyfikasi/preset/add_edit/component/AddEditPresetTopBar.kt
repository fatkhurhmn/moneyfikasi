package dev.muffar.moneyfikasi.preset.add_edit.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.common_ui.component.button.TopBarIconButton
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.extensions.capitalize

@Composable
fun AddEditPresetTopBar(
    isEditMode: Boolean,
    type: TransactionType,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    CommonTopAppBar(
        title = "Preset ${type.name.lowercase().capitalize()}",
        onBackClick = onBackClick,
        action = {
            if (isEditMode) {
                TopBarIconButton(
                    painter = painterResource(R.drawable.ic_delete),
                    color = MaterialTheme.colorScheme.error,
                    onClick = { onDeleteClick() }
                )
            }
        }
    )
}
