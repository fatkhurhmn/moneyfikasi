package dev.muffar.moneyfikasi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.muffar.moneyfikasi.common_ui.theme.MoneyfikasiTheme
import dev.muffar.moneyfikasi.domain.model.EnterPinType
import dev.muffar.moneyfikasi.navigation.Screen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isAppLockEnabled by viewModel.isAppLockEnabled.collectAsStateWithLifecycle()

            if (isAppLockEnabled != null) {
                val startDestination = if (isAppLockEnabled == true) {
                    Screen.EnterPin.routeWithArg(EnterPinType.ENTER_PIN)
                } else {
                    Screen.Home.route
                }

                MoneyfikasiTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
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
