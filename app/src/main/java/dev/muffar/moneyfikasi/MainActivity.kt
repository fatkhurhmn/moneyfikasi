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
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.domain.model.AppLanguage
import dev.muffar.moneyfikasi.domain.model.AppTheme
import dev.muffar.moneyfikasi.domain.model.EnterPinType
import dev.muffar.moneyfikasi.navigation.Screen

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isAppLockEnabled by viewModel.isAppLockEnabled.collectAsStateWithLifecycle()
            val uiSettings by viewModel.uiSettings.collectAsStateWithLifecycle()

            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                viewModel.syncNotificationPermission(isGranted)
            }

            val lifecycleOwner = LocalLifecycleOwner.current
            DisposableEffect(lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_RESUME) {
                        val isEnabled = NotificationManagerCompat.from(this@MainActivity).areNotificationsEnabled()
                        viewModel.syncNotificationPermission(isEnabled)
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }

            LaunchedEffect(Unit) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val isEnabled = NotificationManagerCompat.from(this@MainActivity).areNotificationsEnabled()
                    if (!isEnabled) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            }

            if (isAppLockEnabled != null) {
                val startDestination = if (isAppLockEnabled == true) {
                    Screen.EnterPin.routeWithArg(EnterPinType.ENTER_PIN)
                } else {
                    Screen.Home.route
                }

                val darkTheme = when (uiSettings.appTheme) {
                    AppTheme.LIGHT -> false
                    AppTheme.DARK -> true
                    AppTheme.SYSTEM -> isSystemInDarkTheme()
                }

                val language = when (uiSettings.appLanguage) {
                    AppLanguage.ENGLISH -> "en"
                    AppLanguage.INDONESIAN -> "in"
                    AppLanguage.SYSTEM -> ""
                }

                LaunchedEffect(language) {
                    val appLocale: LocaleListCompat = if (language.isNotEmpty()) {
                        LocaleListCompat.forLanguageTags(language)
                    } else {
                        LocaleListCompat.getEmptyLocaleList()
                    }
                    AppCompatDelegate.setApplicationLocales(appLocale)
                }

                MoneyfikasiTheme(
                    darkTheme = darkTheme
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        MainScreen(
                            navController = rememberNavController(),
                            startDestination = startDestination
                        )
                    }
                }
            }
        }
    }
}
