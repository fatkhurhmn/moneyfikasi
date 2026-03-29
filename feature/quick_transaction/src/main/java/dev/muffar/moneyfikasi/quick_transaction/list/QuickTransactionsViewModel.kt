package dev.muffar.moneyfikasi.quick_transaction.list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class  QuickTransactionsViewModel @Inject constructor(
) : ViewModel() {

    private val _state = MutableStateFlow(QuickTransactionsState())
    val state = _state.asStateFlow()

}
