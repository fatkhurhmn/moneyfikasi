package dev.muffar.moneyfikasi.common_ui.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

object ModifierExt {
    fun Modifier.formModifier(
        paddingValues: PaddingValues,
        scrollState: ScrollState
    ): Modifier {
        return this
            .padding(paddingValues)
            .consumeWindowInsets(paddingValues)
            .imePadding()
            .verticalScroll(scrollState)
            .padding(
                top = 8.dp,
                bottom = 16.dp,
                start = 16.dp,
                end = 16.dp
            )
    }
}