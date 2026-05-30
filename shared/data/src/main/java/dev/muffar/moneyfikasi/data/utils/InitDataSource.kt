package dev.muffar.moneyfikasi.data.utils

import android.content.Context
import dev.muffar.moneyfikasi.data.db.entity.CategoryEntity
import dev.muffar.moneyfikasi.data.db.entity.WalletEntity
import dev.muffar.moneyfikasi.domain.model.AppIcon
import dev.muffar.moneyfikasi.domain.model.CategoryType
import dev.muffar.moneyfikasi.domain.model.Colors
import dev.muffar.moneyfikasi.resource.R
import dev.muffar.moneyfikasi.utils.constants.UUIDConst

object InitDataSource {
    fun getCategories(context: Context): List<CategoryEntity> {
        val expenseCategories = arrayListOf(
            getTransferFeeCategory(context),
            CategoryEntity(
                name = context.getString(R.string.cat_electricity),
                icon = AppIcon.Bolt.name,
                color = Colors.Yellow10.color,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = context.getString(R.string.cat_entertainment),
                icon = AppIcon.ConfirmationNumber.name,
                color = Colors.Blue10.color,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = context.getString(R.string.cat_exercise),
                icon = AppIcon.FitnessCenter.name,
                color = Colors.Brown10.color,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = context.getString(R.string.cat_food_drink),
                icon = AppIcon.Fastfood.name,
                color = Colors.Orange10.color,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = context.getString(R.string.cat_hobby),
                icon = AppIcon.SportsEsports.name,
                color = Colors.Teal10.color,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = context.getString(R.string.cat_health),
                icon = AppIcon.MedicalServices.name,
                color = Colors.Green10.color,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = context.getString(R.string.cat_other),
                icon = AppIcon.Widgets.name,
                color = Colors.Grey10.color,
                type = CategoryType.EXPENSE,
            ),
            getTransferOutCategory(context),
            CategoryEntity(
                name = context.getString(R.string.cat_shopping),
                icon = AppIcon.ShoppingBag.name,
                color = Colors.Orange30.color,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = context.getString(R.string.cat_transport),
                icon = AppIcon.DirectionsCar.name,
                color = Colors.Red20.color,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = context.getString(R.string.cat_travel),
                icon = AppIcon.Flight.name,
                color = Colors.BlueGrey20.color,
                type = CategoryType.EXPENSE,
            ),
            CategoryEntity(
                name = context.getString(R.string.cat_water),
                icon = AppIcon.WaterDrop.name,
                color = Colors.Blue10.color,
                type = CategoryType.EXPENSE,
            )
        )

        val incomeCategories = arrayListOf(
            CategoryEntity(
                name = context.getString(R.string.cat_business),
                icon = AppIcon.BusinessCenter.name,
                color = Colors.Lime20.color,
                type = CategoryType.INCOME,
            ),
            CategoryEntity(
                name = context.getString(R.string.cat_gift),
                icon = AppIcon.CardGiftcard.name,
                color = Colors.Pink10.color,
                type = CategoryType.INCOME,
            ),
            CategoryEntity(
                name = context.getString(R.string.cat_invest),
                icon = AppIcon.TrendingUp.name,
                color = Colors.Cyan10.color,
                type = CategoryType.INCOME,
            ),
            CategoryEntity(
                name = context.getString(R.string.cat_salary),
                icon = AppIcon.Paid.name,
                color = Colors.Green10.color,
                type = CategoryType.INCOME,
            ),
            getTransferInCategory(context)
        )

        val categories = arrayListOf<CategoryEntity>().apply {
            addAll(expenseCategories)
            addAll(incomeCategories)
        }

        return categories
    }

    fun getTransferOutCategory(context: Context) = CategoryEntity(
        id = UUIDConst.TransferOutCategoryId,
        name = context.getString(R.string.cat_transfer),
        icon = AppIcon.SyncAlt.name,
        color = Colors.Red20.color,
        type = CategoryType.EXPENSE,
        isTransferCategory = true
    )

    fun getTransferInCategory(context: Context) = CategoryEntity(
        id = UUIDConst.TransferInCategoryId,
        name = context.getString(R.string.cat_transfer),
        icon = AppIcon.SyncAlt.name,
        color = Colors.Green10.color,
        type = CategoryType.INCOME,
        isTransferCategory = true
    )

    fun getTransferFeeCategory(context: Context) = CategoryEntity(
        id = UUIDConst.TransferFeeCategoryId,
        name = context.getString(R.string.cat_admin_fee),
        icon = AppIcon.Paid.name,
        color = Colors.Purple10.color,
        type = CategoryType.EXPENSE,
        isTransferCategory = true
    )

    fun getWallets(context: Context): List<WalletEntity> {
        return arrayListOf(
            WalletEntity(
                name = context.getString(R.string.wallet_main),
                icon = AppIcon.AccountBalanceWallet.name,
                color = Colors.Blue10.color,
                balance = 0.0,
                isActive = true
            )
        )
    }
}
