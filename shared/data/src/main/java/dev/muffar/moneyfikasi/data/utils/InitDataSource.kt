package dev.muffar.moneyfikasi.data.utils

import dev.muffar.moneyfikasi.data.db.entity.CategoryEntity
import dev.muffar.moneyfikasi.data.db.entity.WalletEntity
import dev.muffar.moneyfikasi.domain.model.AppIcon
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.utils.constants.UUIDConst

object InitDataSource {
    fun getCategories(): List<CategoryEntity> {
        val expenseCategories = arrayListOf(
            TRANSFER_FEE_CATEGORY,
            CategoryEntity(
                name = "Electricity",
                icon = AppIcon.Bolt.name,
                color = 0xFFFFC107,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = "Entertainment",
                icon = AppIcon.ConfirmationNumber.name,
                color = 0xFF7B1FA2,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = "Exercise",
                icon = AppIcon.FitnessCenter.name,
                color = 0xFF6D4C41,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = "Food & drink",
                icon = AppIcon.Fastfood.name,
                color = 0xFFFFEB3B,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = "Hobby",
                icon = AppIcon.SportsEsports.name,
                color = 0xFF00897B,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = "Health",
                icon = AppIcon.MedicalServices.name,
                color = 0xFFC2185B,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = "Other",
                icon = AppIcon.Widgets.name,
                color = 0xFF9E9E9E,
                type = CategoryType.EXPENSE,
            ),
            TRANSFER_OUT_CATEGORY,
            CategoryEntity(
                name = "Shopping",
                icon = AppIcon.ShoppingBag.name,
                color = 0xFFFF9800,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = "Transport",
                icon = AppIcon.DirectionsCar.name,
                color = 0xFFED32F2F,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = "Travel",
                icon = AppIcon.Flight.name,
                color = 0xFF388E3C,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = "Water",
                icon = AppIcon.WaterDrop.name,
                color = 0xFF1976D2,
                type = CategoryType.EXPENSE,
            )
        )

        val incomeCategories = arrayListOf(
            CategoryEntity(
                name = "Business",
                icon = AppIcon.BusinessCenter.name,
                color = 0xFF6D4C41,
                type = CategoryType.INCOME,
            ),
            CategoryEntity(
                name = "Gift",
                icon = AppIcon.CardGiftcard.name,
                color = 0xFFE91E63,
                type = CategoryType.INCOME,
            ),
            CategoryEntity(
                name = "Invest",
                icon = AppIcon.TrendingUp.name,
                color = 0xFF8BC34A,
                type = CategoryType.INCOME,
            ),
            CategoryEntity(
                name = "Salary",
                icon = AppIcon.Paid.name,
                color = 0xFFED32F2F,
                type = CategoryType.INCOME,
            ),
            TRANSFER_IN_CATEGORY
        )

        val categories = arrayListOf<CategoryEntity>().apply {
            addAll(expenseCategories)
            addAll(incomeCategories)
        }

        return categories
    }

    val TRANSFER_OUT_CATEGORY = CategoryEntity(
        id = UUIDConst.TransferOutCategoryId,
        name = "Transfer",
        icon = AppIcon.SyncAlt.name,
        color = 0xFFFC3D56,
        type = CategoryType.EXPENSE,
        isTransferCategory = true
    )

    val TRANSFER_IN_CATEGORY = CategoryEntity(
        id = UUIDConst.TransferInCategoryId,
        name = "Transfer",
        icon = AppIcon.SyncAlt.name,
        color = 0xFF3AAD7A,
        type = CategoryType.INCOME,
        isTransferCategory = true
    )

    val TRANSFER_FEE_CATEGORY = CategoryEntity(
        id = UUIDConst.TransferFeeCategoryId,
        name = "Admin Fee",
        icon = AppIcon.Paid.name,
        color = 0xFF557689,
        type = CategoryType.EXPENSE,
        isTransferCategory = true
    )

    fun getWallets(): List<WalletEntity> {
        return arrayListOf(
            WalletEntity(
                name = "Main",
                icon = AppIcon.AccountBalanceWallet.name,
                color = 0xFF00897B,
                balance = 0.0,
                isActive = true
            )
        )
    }
}