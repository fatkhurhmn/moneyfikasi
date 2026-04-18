package dev.muffar.moneyfikasi.data.utils

import dev.muffar.moneyfikasi.data.db.entity.CategoryEntity
import dev.muffar.moneyfikasi.data.db.entity.WalletEntity
import dev.muffar.moneyfikasi.domain.model.AppIcon
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.Colors
import dev.muffar.moneyfikasi.utils.constants.UUIDConst

object InitDataSource {
    fun getCategories(): List<CategoryEntity> {
        val expenseCategories = arrayListOf(
            TRANSFER_FEE_CATEGORY,
            CategoryEntity(
                name = "Electricity",
                icon = AppIcon.Bolt.name,
                color = Colors.Yellow10.color,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = "Entertainment",
                icon = AppIcon.ConfirmationNumber.name,
                color = Colors.Blue10.color,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = "Exercise",
                icon = AppIcon.FitnessCenter.name,
                color = Colors.Brown10.color,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = "Food & drink",
                icon = AppIcon.Fastfood.name,
                color = Colors.Orange10.color,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = "Hobby",
                icon = AppIcon.SportsEsports.name,
                color = Colors.Teal10.color,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = "Health",
                icon = AppIcon.MedicalServices.name,
                color = Colors.Green10.color,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = "Other",
                icon = AppIcon.Widgets.name,
                color = Colors.Grey10.color,
                type = CategoryType.EXPENSE,
            ),
            TRANSFER_OUT_CATEGORY,
            CategoryEntity(
                name = "Shopping",
                icon = AppIcon.ShoppingBag.name,
                color = Colors.Orange30.color,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = "Transport",
                icon = AppIcon.DirectionsCar.name,
                color = Colors.Red20.color,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = "Travel",
                icon = AppIcon.Flight.name,
                color = Colors.BlueGrey20.color,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = "Water",
                icon = AppIcon.WaterDrop.name,
                color = Colors.Blue10.color,
                type = CategoryType.EXPENSE,
            )
        )

        val incomeCategories = arrayListOf(
            CategoryEntity(
                name = "Business",
                icon = AppIcon.BusinessCenter.name,
                color = Colors.Lime20.color,
                type = CategoryType.INCOME,
            ),
            CategoryEntity(
                name = "Gift",
                icon = AppIcon.CardGiftcard.name,
                color = Colors.Pink10.color,
                type = CategoryType.INCOME,
            ),
            CategoryEntity(
                name = "Invest",
                icon = AppIcon.TrendingUp.name,
                color = Colors.Cyan10.color,
                type = CategoryType.INCOME,
            ),
            CategoryEntity(
                name = "Salary",
                icon = AppIcon.Paid.name,
                color = Colors.Green10.color,
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
        color = Colors.Red20.color,
        type = CategoryType.EXPENSE,
        isTransferCategory = true
    )

    val TRANSFER_IN_CATEGORY = CategoryEntity(
        id = UUIDConst.TransferInCategoryId,
        name = "Transfer",
        icon = AppIcon.SyncAlt.name,
        color = Colors.Green10.color,
        type = CategoryType.INCOME,
        isTransferCategory = true
    )

    val TRANSFER_FEE_CATEGORY = CategoryEntity(
        id = UUIDConst.TransferFeeCategoryId,
        name = "Admin Fee",
        icon = AppIcon.Paid.name,
        color = Colors.Purple10.color,
        type = CategoryType.EXPENSE,
        isTransferCategory = true
    )

    fun getWallets(): List<WalletEntity> {
        return arrayListOf(
            WalletEntity(
                name = "Main",
                icon = AppIcon.AccountBalanceWallet.name,
                color = Colors.Blue10.color,
                balance = 0.0,
                isActive = true
            )
        )
    }
}