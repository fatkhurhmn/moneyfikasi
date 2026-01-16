package dev.muffar.moneyfikasi.data.utils

import dev.muffar.moneyfikasi.data.db.entity.CategoryEntity
import dev.muffar.moneyfikasi.data.db.entity.WalletEntity
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.utils.CategoryIcon
import dev.muffar.moneyfikasi.domain.utils.WalletIcon
import java.util.UUID

object InitDataSource {
    fun getCategories(): List<CategoryEntity> {
        val expenseCategories = arrayListOf(
            ADMIN_TRANSFER_CATEGORY,
            CategoryEntity(
                name = "Electricity",
                icon = CategoryIcon.BOLT.iconName,
                color = 0xFFFFC107,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = "Entertainment",
                icon = CategoryIcon.CONFIRMATION_NUMBER.iconName,
                color = 0xFF7B1FA2,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = "Exercise",
                icon = CategoryIcon.FITNESS_CENTER.iconName,
                color = 0xFF6D4C41,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = "Food & drink",
                icon = CategoryIcon.RAMEN_DINING.iconName,
                color = 0xFFFFEB3B,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = "Hobby",
                icon = CategoryIcon.GAMES.iconName,
                color = 0xFF00897B,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = "Health",
                icon = CategoryIcon.MEDICAL_SERVICES.iconName,
                color = 0xFFC2185B,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = "Other",
                icon = CategoryIcon.WIDGETS.iconName,
                color = 0xFF9E9E9E,
                type = CategoryType.EXPENSE,
            ),
            EXPENSE_TRANSFER_CATEGORY,
            CategoryEntity(
                name = "Shopping",
                icon = CategoryIcon.SHOPPING_CART.iconName,
                color = 0xFFFF9800,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = "Transport",
                icon = CategoryIcon.DIRECTIONS_CAR.iconName,
                color = 0xFFED32F2F,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = "Travel",
                icon = CategoryIcon.MAP.iconName,
                color = 0xFF388E3C,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = "Water",
                icon = CategoryIcon.WATER_DROP.iconName,
                color = 0xFF1976D2,
                type = CategoryType.EXPENSE,
            )
        )

        val incomeCategories = arrayListOf(
            CategoryEntity(
                name = "Business",
                icon = CategoryIcon.BUSINESS_CENTER.iconName,
                color = 0xFF6D4C41,
                type = CategoryType.INCOME,
            ),
            CategoryEntity(
                name = "Gift",
                icon = CategoryIcon.CARD_GIFT_CARD.iconName,
                color = 0xFFE91E63,
                type = CategoryType.INCOME,
            ),
            CategoryEntity(
                name = "Invest",
                icon = CategoryIcon.TIMELINE.iconName,
                color = 0xFF8BC34A,
                type = CategoryType.INCOME,
            ),
            CategoryEntity(
                name = "Salary",
                icon = CategoryIcon.PAID.iconName,
                color = 0xFFED32F2F,
                type = CategoryType.INCOME,
            ),
            INCOME_TRANSFER_CATEGORY
        )

        val categories = arrayListOf<CategoryEntity>().apply {
            addAll(expenseCategories)
            addAll(incomeCategories)
        }

        return categories
    }

    val EXPENSE_TRANSFER_CATEGORY = CategoryEntity(
        id = UUID.fromString("150bbc61-7a8c-4a9a-ab06-da7e4f6d4aa5"),
        name = "Transfer",
        icon = CategoryIcon.TRANSFER.iconName,
        color = 0xFFFC3D56,
        type = CategoryType.EXPENSE,
        isTransferCategory = true
    )

    val INCOME_TRANSFER_CATEGORY = CategoryEntity(
        id = UUID.fromString("25e23912-70c0-4b52-a67c-05b1c786d032"),
        name = "Transfer",
        icon = CategoryIcon.TRANSFER.iconName,
        color = 0xFF3AAD7A,
        type = CategoryType.INCOME,
        isTransferCategory = true
    )

    val ADMIN_TRANSFER_CATEGORY = CategoryEntity(
        id = UUID.fromString("abf1e9b7-c898-4717-9d82-f06dd6c476e1"),
        name = "Admin Fee",
        icon = CategoryIcon.PAID.iconName,
        color = 0xFF557689,
        type = CategoryType.EXPENSE,
        isTransferCategory = true
    )

    fun getWallets(): List<WalletEntity> {
        return arrayListOf(
            WalletEntity(
                name = "Main",
                icon = WalletIcon.ACCOUNT_BALANCE_WALLET.iconName,
                color = 0xFF00897B,
                balance = 0.0,
                isActive = true
            )
        )
    }
}