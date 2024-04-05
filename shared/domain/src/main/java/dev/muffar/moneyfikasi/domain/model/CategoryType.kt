package dev.muffar.moneyfikasi.domain.model

enum class CategoryType {
    EXPENSE,
    INCOME,;

    companion object{
        fun fromString(string: String): CategoryType {
            return entries.first { it.name == string }
        }
    }
}