package dev.muffar.moneyfikasi.preset.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.usecase.preset.PresetUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PresetsViewModel @Inject constructor(
    private val presetUseCases: PresetUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(PresetsState())
    val state = _state.asStateFlow()

    init {
        loadAllPresets()
    }

    private fun loadAllPresets() {
        viewModelScope.launch {
            presetUseCases.getAllPresets()
                .collectLatest { presets ->
                    _state.update { state ->
                        state.copy(presets = presets)
                    }
                }
        }
    }
}
