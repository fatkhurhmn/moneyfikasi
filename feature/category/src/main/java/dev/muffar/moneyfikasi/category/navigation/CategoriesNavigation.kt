package dev.muffar.moneyfikasi.category.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.muffar.moneyfikasi.category.CategoriesScreen
import dev.muffar.moneyfikasi.navigation.Screen

fun NavGraphBuilder.categoriesNavGraph() {
    composable(route = Screen.Categories.route) {
        CategoriesScreen()
    }
}

fun NavController.toCategoriesScreen() {
    navigate(Screen.Categories.route)
}