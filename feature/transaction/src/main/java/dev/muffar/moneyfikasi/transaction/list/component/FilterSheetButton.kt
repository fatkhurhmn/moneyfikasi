package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.button.common.CommonButton
import dev.muffar.moneyfikasi.common_ui.component.button.DoubleOutlinedButton
import dev.muffar.moneyfikasi.resource.R

@Composable
fun FilterSheetButton(
    onCancelClick: () -> Unit,
    onResetClick: () -> Unit,
    onApplyClick: () -> Unit,
) {
    Column {
        CommonHorizontalDivider()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            DoubleOutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                leftText = stringResource(R.string.cancel),
                rightText = stringResource(R.string.reset),
                onLeftClick = onCancelClick,
                onRightClick = onResetClick
            )
            CommonButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.apply),
                onClick = onApplyClick
            )
        }
    }
}