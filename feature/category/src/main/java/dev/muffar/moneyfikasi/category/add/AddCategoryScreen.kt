package dev.muffar.moneyfikasi.category.add

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.category.add.component.AddCategoryBottomSheet
import dev.muffar.moneyfikasi.category.add.component.AddCategoryForm
import dev.muffar.moneyfikasi.category.add.component.AddCategorySheetContent
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.resource.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryScreen(
    modifier: Modifier = Modifier,
    state: AddCategoryState,
    onNameChange: (String) -> Unit,
    onIconChange: (String) -> Unit,
    onColorChange: (Long) -> Unit,
    onShowBottomSheet: (AddCategoryBottomSheet?) -> Unit,
    onBackClick: () -> Unit,
) {
    val title = if (state.type == CategoryType.INCOME) {
        stringResource(R.string.income_category)
    } else {
        stringResource(R.string.expense_category)
    }

    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = title,
                onBackClick = onBackClick
            )
        },
    ) {
        Box(modifier = modifier.padding(it)) {
            AddCategoryForm(
                name = state.name,
                icon = state.icon,
                color = state.color,
                onNameChange = onNameChange,
                onIconClick = {
                    onShowBottomSheet(AddCategoryBottomSheet.ICON)
                },
                onColorClick = {
                    onShowBottomSheet(AddCategoryBottomSheet.COLOR)
                }
            )

            if (state.bottomSheetType != null) {
                ModalBottomSheet(
                    onDismissRequest = { onShowBottomSheet(null) },
                    sheetState = sheetState
                ) {
                    AddCategorySheetContent(
                        type = state.bottomSheetType,
                        categoryType = state.type,
                        onIconSelect = { icon ->
                            onIconChange(icon)
                            onShowBottomSheet(null)
                        },
                        onColorSelect = { color ->
                            onColorChange(color)
                        },
                        onDismiss = { onShowBottomSheet(null) }
                    )
                }
            }
        }
    }
}