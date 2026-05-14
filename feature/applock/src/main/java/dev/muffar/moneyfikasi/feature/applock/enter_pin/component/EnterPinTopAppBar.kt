package dev.muffar.moneyfikasi.feature.applock.enter_pin.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.top_bar.CommonTopAppBar
import dev.muffar.moneyfikasi.domain.model.EnterPinType
import dev.muffar.moneyfikasi.resource.R

@Composable
fun EnterPinTopAppBar(
    type: EnterPinType,
    onBackClick: () -> Unit
) {
    if (type == EnterPinType.ENTER_PIN) return
    CommonTopAppBar(
        title = when (type) {
            EnterPinType.SET_PIN -> stringResource(R.string.set_pin)
            EnterPinType.RESET_PIN -> stringResource(R.string.reset_pin)
            EnterPinType.DISABLE_PIN -> stringResource(R.string.disable_app_lock)
            else -> stringResource(R.string.app_lock)
        },
        onBackClick = onBackClick
    )
}