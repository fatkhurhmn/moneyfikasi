package dev.muffar.moneyfikasi.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.AppLanguage
import dev.muffar.moneyfikasi.domain.model.AppTheme
import dev.muffar.moneyfikasi.domain.model.AmountInputType
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
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
    private val categoryUseCases: CategoryUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            uiSettingsUseCases.getUiSettings().collectLatest { uiSettings ->
                _state.update {
                    it.copy(
                        appTheme = uiSettings.appTheme,
                        appLanguage = uiSettings.appLanguage,
                        amountInputType = uiSettings.amountInputType,
                    )
                }
            }
        }
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.AppThemeChanged -> onAppThemeChange(event.theme)
            is SettingsEvent.AppLanguageChanged -> onAppLanguageChange(event.language)
            is SettingsEvent.AmountInputTypeChanged -> onAmountInputTypeChange(event.type)
        }
    }

    private fun onAppThemeChange(theme: AppTheme) {
        viewModelScope.launch {
            uiSettingsUseCases.setAppTheme(theme)
        }
    }

    private fun onAppLanguageChange(language: AppLanguage) {
        viewModelScope.launch {
            uiSettingsUseCases.setAppLanguage(language)
            categoryUseCases.updateDefaultCategories(language)
        }
    }

    private fun onAmountInputTypeChange(type: AmountInputType) {
        viewModelScope.launch {
            uiSettingsUseCases.setAmountInputType(type)
        }
    }
}
