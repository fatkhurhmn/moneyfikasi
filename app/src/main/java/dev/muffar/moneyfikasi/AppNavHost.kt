package dev.muffar.moneyfikasi

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.muffar.moneyfikasi.navigation.Screen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
    builder: NavGraphBuilder.() -> Unit,
) {
    val bottomNavRoutes = listOf(
        Screen.Home.route,
        Screen.Transactions.route,
        Screen.Statistic.route,
        Screen.More.route
    )

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = {
            val initialRoute = initialState.destination.route
            val targetRoute = targetState.destination.route
            if (initialRoute in bottomNavRoutes && targetRoute in bottomNavRoutes) {
                val initialIndex = bottomNavRoutes.indexOf(initialRoute)
                val targetIndex = bottomNavRoutes.indexOf(targetRoute)
                if (targetIndex > initialIndex) {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300)
                    )
                } else {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(300)
                    )
                }
            } else {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            }
        },
        exitTransition = {
            val initialRoute = initialState.destination.route
            val targetRoute = targetState.destination.route
            if (initialRoute in bottomNavRoutes && targetRoute in bottomNavRoutes) {
                val initialIndex = bottomNavRoutes.indexOf(initialRoute)
                val targetIndex = bottomNavRoutes.indexOf(targetRoute)
                if (targetIndex > initialIndex) {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300)
                    )
                } else {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(300)
                    )
                }
            } else {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            }
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        },
        builder = builder
    )
}
