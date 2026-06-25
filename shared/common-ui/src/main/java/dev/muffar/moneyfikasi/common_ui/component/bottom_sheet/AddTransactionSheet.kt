package dev.muffar.moneyfikasi.common_ui.component.bottom_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowDown
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowUp
import androidx.compose.material.icons.rounded.SwapHoriz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.icon.ArrowRight
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionSheet(
    onAddExpense: () -> Unit,
    onAddIncome: () -> Unit,
    onAddTransfer: () -> Unit,
    onAddAi: () -> Unit,
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
        BottomSheetTitle(stringResource(R.string.title_create_transaction))
        Spacer(Modifier.height(16.dp))
        AddTransactionItem(
            imageVector = Icons.Rounded.AutoAwesome,
            title = stringResource(id = R.string.label_ai_transaction),
            color = MaterialTheme.colorScheme.primary,
            onClick = {
                hideSheet()
                onAddAi()
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        AddTransactionItem(
            imageVector = Icons.Rounded.KeyboardDoubleArrowDown,
            title = stringResource(id = R.string.label_income),
            color = MoneyfikasiTheme.financeColors.income,
            onClick = {
                hideSheet()
                onAddIncome()
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        AddTransactionItem(
            imageVector = Icons.Rounded.KeyboardDoubleArrowUp,
            title = stringResource(id = R.string.label_expense),
            color = MoneyfikasiTheme.financeColors.expense,
            onClick = {
                hideSheet()
                onAddExpense()
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        AddTransactionItem(
            imageVector = Icons.Rounded.SwapHoriz,
            title = stringResource(id = R.string.label_transfer),
            color = MoneyfikasiTheme.financeColors.info,
            onClick = {
                hideSheet()
                onAddTransfer()
            }
        )
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun AddTransactionItem(
    imageVector: ImageVector,
    title: String,
    color: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(color.copy(alpha = 0.05f))
            .border(
                width = 1.dp,
                color = color.copy(alpha = 0.1f),
                shape = MaterialTheme.shapes.medium
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = title,
            modifier = Modifier.size(36.dp),
            tint = color.copy(alpha = 0.8f)
        )
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        ArrowRight(20.dp)
    }
}