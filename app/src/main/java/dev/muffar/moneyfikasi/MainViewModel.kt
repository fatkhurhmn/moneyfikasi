package dev.muffar.moneyfikasi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.usecase.preferences.security.SecuritySettingsUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val securitySettingsUseCases: SecuritySettingsUseCases,
) : ViewModel() {

    private val _isAppLockEnabled = MutableStateFlow<Boolean?>(null)
    val isAppLockEnabled = _isAppLockEnabled.asStateFlow()

    init {
        checkAppLock()
    }

    private fun checkAppLock() {
        viewModelScope.launch {
            val isEnabled = securitySettingsUseCases.isAppLockEnabled()
                .first()
            _isAppLockEnabled.update { isEnabled }
        }
    }
}
