package dev.muffar.moneyfikasi.statistic.detail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.Transaction
import dev.muffar.moneyfikasi.domain.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class StatisticDetailViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(StatisticDetailState())
    val state = _state.asStateFlow()


    fun onEvent(event: StatisticDetailEvent) {
        when (event) {
            is StatisticDetailEvent.OnInitState -> initState(event.transactions, event.type)
        }
    }

    private fun initState(transactions: List<Transaction>, type: TransactionType) {
        _state.update { it.copy(transactions = transactions, type = type) }
    }
}