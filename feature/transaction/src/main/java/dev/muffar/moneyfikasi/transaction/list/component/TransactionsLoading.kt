package dev.muffar.moneyfikasi.transaction.list.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer

@Composable
fun TransactionsLoading(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
    ) {
        LazyColumn {
            item { TransactionShimmer() }
            item { TransactionShimmer() }
            item { TransactionShimmer() }
            item { TransactionShimmer() }
            item { TransactionShimmer() }
            item { TransactionShimmer() }
        }
    }
}

@Composable
fun TransactionShimmer() {
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(MaterialTheme.shapes.medium)
            .shimmer()
            .background(MaterialTheme.colorScheme.outline.copy(0.2f))
            .fillMaxWidth()
            .height(60.dp)
    )
}