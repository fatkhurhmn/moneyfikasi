package dev.muffar.moneyfikasi.data.utils

import dev.muffar.moneyfikasi.data.db.entity.CategoryEntity
import dev.muffar.moneyfikasi.data.db.entity.WalletEntity
import java.util.UUID

object InitDataSource {
    fun getCategories(): List<CategoryEntity> {
        val expenseCategories = arrayListOf(
            CategoryEntity(
                id = UUID.randomUUID(),
                name = "Electricity",
                icon = ExpenseCategoryIcon.BOLT.iconName,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                id = UUID.randomUUID(),
                name = "Entertainment",
                icon = ExpenseCategoryIcon.CONFIRMATION_NUMBER.iconName,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                id = UUID.randomUUID(),
                name = "Exercise",
                icon = ExpenseCategoryIcon.FITNESS_CENTER.iconName,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                id = UUID.randomUUID(),
                name = "Food & drink",
                icon = ExpenseCategoryIcon.RAMEN_DINING.iconName,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                id = UUID.randomUUID(),
                name = "Hobby",
                icon = ExpenseCategoryIcon.GAMES.iconName,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                id = UUID.randomUUID(),
                name = "Health",
                icon = ExpenseCategoryIcon.MEDICAL_SERVICES.iconName,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                id = UUID.randomUUID(),
                name = "Other",
                icon = ExpenseCategoryIcon.WIDGETS.iconName,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                id = UUID.randomUUID(),
                name = "Shopping",
                icon = ExpenseCategoryIcon.SHOPPING_CART.iconName,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                id = UUID.randomUUID(),
                name = "Transport",
                icon = ExpenseCategoryIcon.DIRECTIONS_CAR.iconName,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                id = UUID.randomUUID(),
                name = "Travel",
                icon = ExpenseCategoryIcon.MAP.iconName,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                id = UUID.randomUUID(),
                name = "Water",
                icon = ExpenseCategoryIcon.WATER_DROP.iconName,
                type = CategoryType.EXPENSE,
            )
        )

        val incomeCategories = arrayListOf(
            CategoryEntity(
                id = UUID.randomUUID(),
                name = "Business",
                icon = IncomeCategoryIcon.BUSINESS_CENTER.iconName,
                type = CategoryType.INCOME,
            ),
            CategoryEntity(
                id = UUID.randomUUID(),
                name = "Gift",
                icon = IncomeCategoryIcon.CARD_GIFT_CARD.iconName,
                type = CategoryType.INCOME,
            ),
            CategoryEntity(
                id = UUID.randomUUID(),
                name = "Invest",
                icon = IncomeCategoryIcon.TIMELINE.iconName,
                type = CategoryType.INCOME,
            ),
            CategoryEntity(
                id = UUID.randomUUID(),
                name = "Salary",
                icon = IncomeCategoryIcon.PAID.iconName,
                type = CategoryType.INCOME,
            ),
        )

        val categories = arrayListOf<CategoryEntity>().apply {
            addAll(expenseCategories)
            addAll(incomeCategories)
        }

        return categories
    }

    fun getWallets(): List<WalletEntity> {
        return arrayListOf(
            WalletEntity(
                id = UUID.randomUUID(),
                name = "Main",
                icon = WalletIcon.ACCOUNT_BALANCE_WALLET.iconName,
                balance = 0.0,
                isActive = true
            )
        )
    }
}