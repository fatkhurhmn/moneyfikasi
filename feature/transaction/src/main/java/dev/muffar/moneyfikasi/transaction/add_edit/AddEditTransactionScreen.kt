package dev.muffar.moneyfikasi.transaction.add_edit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.muffar.moneyfikasi.common_ui.component.CommonTopAppBar

@Composable
fun AddEditTransactionScreen(
    modifier: Modifier = Modifier,
    state: AddEditTransactionState,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = state.type.value,
                onBackClick = onBackClick
            )
        },
    ) {
        Box(modifier = modifier.padding(it)) {}
    }
}