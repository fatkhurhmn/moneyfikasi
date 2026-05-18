package dev.muffar.moneyfikasi.common_ui.component.bottom_sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.SettingsSuggest
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.button.DoubleOutlinedButton
import dev.muffar.moneyfikasi.domain.model.AppTheme
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemePickerSheet(
    selectedTheme: AppTheme,
    onThemeSelect: (AppTheme) -> Unit,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val hideSheet = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismissRequest()
            }
        }
    }

    ModalBottomSheet(
        modifier = Modifier.statusBarsPadding(),
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        sheetGesturesEnabled = false
    ) {
        BottomSheetTitle(stringResource(R.string.select_theme))
        val options = AppTheme.entries
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(options) { option ->
                val icon = when (option) {
                    AppTheme.LIGHT -> Icons.Rounded.LightMode
                    AppTheme.DARK -> Icons.Rounded.DarkMode
                    AppTheme.SYSTEM -> Icons.Rounded.SettingsSuggest
                }

                val title = when (option) {
                    AppTheme.LIGHT -> stringResource(R.string.light)
                    AppTheme.DARK -> stringResource(R.string.dark)
                    AppTheme.SYSTEM -> stringResource(R.string.system_default)
                }

                PickerOptionItem(
                    isSelected = selectedTheme == option,
                    icon = icon,
                    title = title,
                    onClick = {
                        hideSheet()
                        onThemeSelect(option)
                    }
                )
            }
        }

        CommonHorizontalDivider()
        DoubleOutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            leftText = stringResource(R.string.cancel),
            rightText = stringResource(R.string.reset),
            onLeftClick = { hideSheet() },
            onRightClick = {
                hideSheet()
                onThemeSelect(AppTheme.SYSTEM)
            }
        )
    }
}
