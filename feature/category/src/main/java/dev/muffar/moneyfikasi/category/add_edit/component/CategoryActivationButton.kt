package dev.muffar.moneyfikasi.category.add_edit.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonSwitch
import dev.muffar.moneyfikasi.resource.R

@Composable
fun CategoryActivationButton(
    isActive: Boolean,
    onIsActiveChange: () -> Unit
) {
    CommonSwitch(
        isEnabled = isActive,
        onEnabledChange = {
            onIsActiveChange()
        },
        title = stringResource(R.string.label_activation),
        titleStyle = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
        description = stringResource(R.string.msg_disable_category_description),
        clickableText = false
    )
}