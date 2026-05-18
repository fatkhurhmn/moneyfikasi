package dev.muffar.moneyfikasi.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.AppTheme
import dev.muffar.moneyfikasi.domain.usecase.preferences.ui.UiSettingsUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val uiSettingsUseCases: UiSettingsUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            uiSettingsUseCases.getUiSettings().collectLatest { uiSettings ->
                _state.update { it.copy(appTheme = uiSettings.appTheme) }
            }
        }
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.OnAppThemeChanged -> onAppThemeChanged(event.theme)
        }
    }

    private fun onAppThemeChanged(theme: AppTheme) {
        viewModelScope.launch {
            uiSettingsUseCases.setAppTheme(theme)
        }
    }
}
