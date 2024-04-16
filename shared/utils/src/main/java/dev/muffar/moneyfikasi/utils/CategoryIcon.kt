package dev.muffar.moneyfikasi.utils

import dev.muffar.moneyfikasi.domain.model.CategoryType

enum class CategoryIcon(val iconName: String, val categoryType: CategoryType) {
    APARTMENT("Apartment", CategoryType.EXPENSE),
    BOLT("Bolt", CategoryType.EXPENSE),
    CALL("Call", CategoryType.EXPENSE),
    CAKE("Cake", CategoryType.EXPENSE),
    CHILD_CARE("ChildCare", CategoryType.EXPENSE),
    CHILD_FRIENDLY("ChildFriendly", CategoryType.EXPENSE),
    COFFEE("Coffee", CategoryType.EXPENSE),
    COLLECTIONS_BOOKMARK("CollectionsBookmark", CategoryType.EXPENSE),
    CONFIRMATION_NUMBER("ConfirmationNumber", CategoryType.EXPENSE),
    DIRECTIONS_CAR("DirectionsCar", CategoryType.EXPENSE),
    FAVORITE("Favorite", CategoryType.EXPENSE),
    FITNESS_CENTER("FitnessCenter", CategoryType.EXPENSE),
    FLIGHT("Flight", CategoryType.EXPENSE),
    GAMES("Games", CategoryType.EXPENSE),
    HOME("Home", CategoryType.EXPENSE),
    KITCHEN("Kitchen", CategoryType.EXPENSE),
    LAPTOP("Laptop", CategoryType.EXPENSE),
    LOCAL_BAR("LocalBar", CategoryType.EXPENSE),
    LOCAL_MALL("LocalMall", CategoryType.EXPENSE),
    MEDICAL_SERVICES("MedicalServices", CategoryType.EXPENSE),
    MAP("Map", CategoryType.EXPENSE),
    MOVIE("Movie", CategoryType.EXPENSE),
    PETS("Pets", CategoryType.EXPENSE),
    POOL("Pool", CategoryType.EXPENSE),
    RAMEN_DINING("RamenDining", CategoryType.EXPENSE),
    RESTAURANT("Restaurant", CategoryType.EXPENSE),
    SELF_IMPROVEMENT("SelfImprovement", CategoryType.EXPENSE),
    SHOPPING_CART("ShoppingCart", CategoryType.EXPENSE),
    SMARTPHONE("Smartphone", CategoryType.EXPENSE),
    SPORTS_ESPORTS("SportsEsports", CategoryType.EXPENSE),
    SPORTS_SOCCER("SportsSoccer", CategoryType.EXPENSE),
    STORE1("Store", CategoryType.EXPENSE),
    SYNC_ALT("SyncAlt", CategoryType.EXPENSE),
    TRAIN("Train", CategoryType.EXPENSE),
    TRANSFER2("SyncAlt", CategoryType.EXPENSE),
    TWO_WHEELER("TwoWheeler", CategoryType.EXPENSE),
    WATER_DROP("WaterDrop", CategoryType.EXPENSE),
    WIDGETS("Widgets", CategoryType.EXPENSE),
    WORK("Work", CategoryType.EXPENSE),

    PAID("Paid", CategoryType.INCOME),
    BUSINESS_CENTER("BusinessCenter", CategoryType.INCOME),
    CARD_GIFT_CARD("CardGiftcard", CategoryType.INCOME),
    EMOJI_EVENT("EmojiEvents", CategoryType.INCOME),
    TIMELINE("Timeline", CategoryType.INCOME),
    SYNC_ALT2("SyncAlt", CategoryType.INCOME),
    STORE2("Store", CategoryType.INCOME),
    SMART_DISPLAY("SmartDisplay", CategoryType.INCOME),
    TRANSFER("SyncAlt", CategoryType.INCOME),
    ;

    companion object {
        fun getCategories(type: CategoryType) = entries
            .filter { it.categoryType == type }
            .map { it.iconName }
    }
}