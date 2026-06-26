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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.muffar.moneyfikasi.common_ui.component.bottom_sheet.AiTransactionSheet
import dev.muffar.moneyfikasi.common_ui.component.message.SnackbarMessage
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.domain.model.AppLanguage
import dev.muffar.moneyfikasi.domain.model.AppTheme
import dev.muffar.moneyfikasi.domain.model.TransactionType
import dev.muffar.moneyfikasi.domain.model.UiSettings
import dev.muffar.moneyfikasi.navigation.MainBottomNav
import dev.muffar.moneyfikasi.navigation.MainNavigation
import dev.muffar.moneyfikasi.navigation.Screen
import dev.muffar.moneyfikasi.transaction.add_edit.navigation.toAddEditTransactionScreen
import dev.muffar.moneyfikasi.transaction.transfer.navigation.toTransferTransactionScreen
import dev.muffar.moneyfikasi.utils.extensions.DoubleExt.formatThousand
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainScreen(
    postSplashRoute: String?,
    uiSettings: UiSettings,
    viewModel: MainViewModel
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

    var showAiDialog by remember { mutableStateOf(false) }
    val isAiProcessing by viewModel.isAiProcessing.collectAsState()
    val aiError by viewModel.aiError.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.aiEventFlow.collectLatest { event ->
            when (event) {
                is MainViewModel.AiEvent.Success -> {
                    showAiDialog = false
                    viewModel.clearAiError()
                    if (event.result.type == TransactionType.TRANSFER_OUT) {
                        navController.toTransferTransactionScreen(
                            amount = event.result.amount.formatThousand(),
                            note = event.result.note,
                            fromWallet = event.result.fromWallet,
                            toWallet = event.result.toWallet,
                            fee = event.result.fee?.formatThousand()
                        )
                    } else {
                        navController.toAddEditTransactionScreen(
                            type = event.result.type,
                            amount = event.result.amount.formatThousand(),
                            note = event.result.note,
                            category = event.result.category,
                            wallet = event.result.wallet
                        )
                    }
                }

                is MainViewModel.AiEvent.Error -> {
                    // Handled in sheet
                }
            }
        }
    }

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
                contentWindowInsets = WindowInsets(0.dp),
                snackbarHost = { SnackbarMessage(state = snackbarHostState) }
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
                            },
                            onAddAiTransaction = {
                                showAiDialog = true
                            }
                        )
                    }

                    if (showAiDialog) {
                        AiTransactionSheet(
                            onDismissRequest = {
                                showAiDialog = false
                                viewModel.clearAiError()
                            },
                            onConfirm = { prompt ->
                                viewModel.parseAiTransaction(prompt)
                            },
                            isProcessing = isAiProcessing,
                            error = aiError?.toMessageRes()?.let { res ->
                                stringResource(res)
                            }
                        )
                    }
                }
            }
        }
    }
}
