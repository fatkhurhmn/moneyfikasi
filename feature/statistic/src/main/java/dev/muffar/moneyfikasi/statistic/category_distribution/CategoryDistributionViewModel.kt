package dev.muffar.moneyfikasi.statistic.category_distribution

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.muffar.moneyfikasi.domain.model.DateRange
import dev.muffar.moneyfikasi.domain.usecase.category.CategoryUseCases
import dev.muffar.moneyfikasi.domain.usecase.statistic.StatisticUseCases
import dev.muffar.moneyfikasi.domain.usecase.wallet.WalletUseCases
import dev.muffar.moneyfikasi.navigation.Screen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryDistributionViewModel @Inject constructor(
    private val statisticUseCases: StatisticUseCases,
    private val categoryUseCases: CategoryUseCases,
    private val walletUseCases: WalletUseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val startDate = savedStateHandle.get<Long>(Screen.CategoryDistribution.START_DATE) ?: 0L
    private val endDate = savedStateHandle.get<Long>(Screen.CategoryDistribution.END_DATE) ?: 0L

    private val _state = MutableStateFlow(CategoryDistributionState())
    val state = _state.asStateFlow()

    init {
        observeCategoryStatistics()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeCategoryStatistics() {
        viewModelScope.launch {
            combine(
                categoryUseCases.getAllCategories(true),
                walletUseCases.getAllWallets()
            ) { categories, wallets ->
                DateRange(
                    start = startDate,
                    end = endDate
                ) to (categories.toSet() to wallets.toSet())
            }.flatMapLatest { (dateRange, pair) ->
                val (categories, wallets) = pair
                statisticUseCases.getCategoryStatistics(dateRange, categories, wallets)
            }.collectLatest { categoryStats ->
                _state.update { it.copy(categoryStatistics = categoryStats) }
            }
        }
    }
}
