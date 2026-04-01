package dev.muffar.moneyfikasi.preset.list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class  PresetListViewModel @Inject constructor(
) : ViewModel() {

    private val _state = MutableStateFlow(PresetListState())
    val state = _state.asStateFlow()

}
