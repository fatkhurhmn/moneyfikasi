package dev.muffar.moneyfikasi.category.add

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.resource.R

@Composable
fun AddCategoryScreen(
    modifier: Modifier = Modifier,
    state: AddCategoryState,
    onBackClick: () -> Unit,
) {
    val title = if (state.type == CategoryType.INCOME) {
        stringResource(R.string.income_category)
    } else {
        stringResource(R.string.expense_category)
    }

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = title,
                onBackClick = onBackClick
            )
        }
    ) {
        Box(modifier = modifier.padding(it)) {
        }
    }
}