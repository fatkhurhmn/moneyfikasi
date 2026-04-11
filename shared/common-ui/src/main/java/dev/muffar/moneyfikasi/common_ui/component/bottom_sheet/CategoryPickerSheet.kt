package dev.muffar.moneyfikasi.common_ui.component.bottom_sheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.muffar.moneyfikasi.common_ui.component.CommonHorizontalDivider
import dev.muffar.moneyfikasi.common_ui.component.EmptyDataList
import dev.muffar.moneyfikasi.common_ui.component.button.DoubleOutlinedButton
import dev.muffar.moneyfikasi.common_ui.component.transaction_item.ItemCategoryIcon
import dev.muffar.moneyfikasi.domain.model.Category
import dev.muffar.moneyfikasi.resource.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryPickerSheet(
    categoryOptions: List<Category>,
    onCategorySelect: (Category) -> Unit,
    onAddNewCategoryClick: () -> Unit,
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
        containerColor = MaterialTheme.colorScheme.surface,
        sheetGesturesEnabled = false
    ) {
        BottomSheetTitle(stringResource(R.string.select_category))
        if (categoryOptions.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(categoryOptions) { category ->
                    CategoryOptionItem(
                        category = category,
                        onClick = {
                            hideSheet()
                            onCategorySelect(category)
                        }
                    )
                }
            }
        } else {
            EmptyDataList(
                painter = painterResource(id = R.drawable.ic_no_category),
                title = stringResource(id = R.string.no_categories),
                description = stringResource(id = R.string.no_categories_message),
                modifier = Modifier.weight(1f)
            )
        }

        CommonHorizontalDivider()
        DoubleOutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            leftText = stringResource(R.string.cancel),
            rightText = stringResource(R.string.create),
            onLeftClick = { hideSheet() },
            onRightClick = {
                hideSheet()
                onAddNewCategoryClick()
            }
        )
    }
}

@Composable
private fun CategoryOptionItem(
    category: Category,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ItemCategoryIcon(category)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = category.name)
    }
}