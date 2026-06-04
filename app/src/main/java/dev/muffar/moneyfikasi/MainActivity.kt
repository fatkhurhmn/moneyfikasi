package dev.muffar.moneyfikasi

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.domain.model.AppLanguage
import dev.muffar.moneyfikasi.domain.model.AppTheme
import dev.muffar.moneyfikasi.domain.model.UiSettings
import dev.muffar.moneyfikasi.navigation.Screen

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val postSplashRoute by viewModel.postSplashRoute.collectAsStateWithLifecycle()
            val uiSettings by viewModel.uiSettings.collectAsStateWithLifecycle()

            AppContent(
                postSplashRoute = postSplashRoute,
                uiSettings = uiSettings
            )

            if (postSplashRoute != null) {
                NotificationPermissionHandler(
                    onPermissionResult = viewModel::syncNotificationPermission,
                    onSyncPermission = {
                        val isEnabled = NotificationManagerCompat.from(this).areNotificationsEnabled()
                        viewModel.syncNotificationPermission(isEnabled)
                    }
                )
            }
        }
    }

    @Composable
    private fun NotificationPermissionHandler(
        onPermissionResult: (Boolean) -> Unit,
        onSyncPermission: () -> Unit,
    ) {
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = onPermissionResult
        )

        val lifecycleOwner = LocalLifecycleOwner.current
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    onSyncPermission()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
        }

        LaunchedEffect(Unit) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val isEnabled = NotificationManagerCompat.from(this@MainActivity).areNotificationsEnabled()
                if (!isEnabled) {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }

    @Composable
    private fun AppContent(
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

        MoneyfikasiTheme(darkTheme = darkTheme) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surface
            ) {
                MainScreen(
                    navController = navController,
                    startDestination = Screen.Splash.route
                )
            }
        }
    }
}
