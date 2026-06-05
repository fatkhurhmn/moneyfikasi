package dev.muffar.moneyfikasi

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.domain.model.AppLanguage
import dev.muffar.moneyfikasi.domain.model.AppTheme
import dev.muffar.moneyfikasi.domain.model.UiSettings
import dev.muffar.moneyfikasi.navigation.MainBottomNav
import dev.muffar.moneyfikasi.navigation.MainNavigation
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.transaction.add_edit.navigation.toAddEditTransactionScreen
import dev.muffar.moneyfikasi.transaction.transfer.navigation.toTransferTransactionScreen

@Composable
fun MainScreen(
    postSplashRoute: String?,
    uiSettings: UiSettings,
) {
    val darkTheme = when (uiSettings.appTheme) {
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        AppTheme.SYSTEM -> isSystemInDarkTheme()
    }

    val languageTag = when (uiSettings.appLanguage) {
        AppLanguage.ENGLISH -> "en"
        AppLanguage.INDONESIAN -> "in"
        AppLanguage.SYSTEM -> ""
    }

    LaunchedEffect(uiSettings.appTheme) {
        AppCompatDelegate.setDefaultNightMode(uiSettings.appTheme.toAppCompatNightMode())
    }

    LaunchedEffect(languageTag) {
        val appLocale: LocaleListCompat = if (languageTag.isNotEmpty()) {
            LocaleListCompat.forLanguageTags(languageTag)
        } else {
            LocaleListCompat.getEmptyLocaleList()
        }
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(postSplashRoute, navBackStackEntry) {
        if (postSplashRoute != null && navBackStackEntry?.destination?.route == Screen.Splash.route) {
            navController.navigate(postSplashRoute) {
                popUpTo(Screen.Splash.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    val mainRoute = listOf(
        Screen.Home.route,
        Screen.Transactions.route,
        Screen.Statistic.route,
        Screen.More.route,
    )

    val currentRoute = navBackStackEntry?.destination?.route
    val isBottomNavVisible = mainRoute.contains(currentRoute)

    MoneyfikasiTheme(darkTheme = darkTheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
        ) {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.surface,
                contentWindowInsets = WindowInsets(0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .consumeWindowInsets(it)
                ) {
                    MainNavigation(
                        navController = navController,
                        startDestination = Screen.Splash.route
                    )

                    AnimatedVisibility(
                        visible = isBottomNavVisible,
                        enter = fadeIn() + slideInVertically { it },
                        exit = fadeOut() + slideOutVertically { it },
                        modifier = Modifier.align(Alignment.BottomCenter)
                    ) {
                        MainBottomNav(
                            navController = navController,
                            onAddTransaction = { type ->
                                if (type != null) {
                                    navController.toAddEditTransactionScreen(type)
                                } else {
                                    navController.toTransferTransactionScreen()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
