package dev.muffar.moneyfikasi

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import dev.muffar.moneyfikasi.navigation.MainBottomNav

@Composable
fun MoneyfikasiApp(
    navController: NavHostController,
) {
    Scaffold(
        bottomBar = { MainBottomNav(navController = navController) },
        floatingActionButton = { AddActionButton() },
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {
            RootNavigation(
                navController = navController
            )
        }
    }
}

@Composable
fun AddActionButton(
    onClick: () -> Unit = {},
) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = "Add",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}