package dev.muffar.moneyfikasi.statistic.category_distribution

import dev.muffar.moneyfikasi.domain.model.CategoryStatistic
import dev.muffar.moneyfikasi.domain.model.CategoryType

data class CategoryDistributionState(
    val categoryStatistics: Map<CategoryType, List<CategoryStatistic>> = emptyMap(),
    val isLoading: Boolean = false,
)
