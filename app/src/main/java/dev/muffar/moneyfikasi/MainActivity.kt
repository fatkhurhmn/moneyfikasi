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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val postSplashRoute by viewModel.postSplashRoute.collectAsStateWithLifecycle()
            val uiSettings by viewModel.uiSettings.collectAsStateWithLifecycle()

            MainScreen(
                postSplashRoute = postSplashRoute,
                uiSettings = uiSettings,
                viewModel = viewModel
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
}
