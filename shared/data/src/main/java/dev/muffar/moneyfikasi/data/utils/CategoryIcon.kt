package dev.muffar.moneyfikasi.data.utils

interface CategoryIcon

enum class ExpenseCategoryIcon(val iconName: String, val iconColor: Long) : CategoryIcon {
    APARTMENT("Apartment", 0xFF219ebc),
    BOLT("Bolt", 0xFFe9c46a),
    CALL("Call", 0xFFd4a373),
    CAKE("Cake", 0xFF00b4d8),
    CHILD_CARE("ChildCare", 0xFFfdf0d5),
    CHILD_FRIENDLY("ChildFriendly", 0xFFffd60a),
    COFFEE("Coffee", 0xFFbc4749),
    COLLECTIONS_BOOKMARK("CollectionsBookmark", 0xFF0077b6),
    CONFIRMATION_NUMBER("ConfirmationNumber", 0xFFa7c957),
    DIRECTIONS_CAR("DirectionsCar", 0xFFef233c),
    FAVORITE("Favorite", 0xFFfcf6bd),
    FITNESS_CENTER("FitnessCenter", 0xFF023047),
    FLIGHT("Flight", 0xFF264653),
    GAMES("Games", 0xFFe76f51),
    HOME("Home", 0xFFffbe0b),
    KITCHEN("Kitchen", 0xFF38a3a5),
    LAPTOP("Laptop", 0xFF3a7ca5),
    LOCAL_BAR("LocalBar", 0xFFd1495b),
    LOCAL_MALL("LocalMall", 0xFF555b6e),
    MEDICAL_SERVICES("MedicalServices", 0xFFee6c4d),
    MAP("Map", 0xFF3d5a80),
    MOVIE("Movie", 0xFF94e8b4),
    PETS("Pets", 0xFF25ced1),
    POOL("Pool", 0xFFea526f),
    RAMEN_DINING("RamenDining", 0xFF324376),
    RESTAURANT("Restaurant", 0xFFf68e5f),
    SELF_IMPROVEMENT("SelfImprovement", 0xFF70e000),
    SHOPPING_CART("ShoppingCart", 0xFFb298dc),
    SMARTPHONE("Smartphone", 0xFFa77b4c),
    SPORTS_ESPORTS("SportsEsports", 0xFF4f6d7a),
    SPORTS_SOCCER("SportsSoccer", 0xFF003459),
    STORE("Store", 0xFF2a9d8f),
    SYNC_ALT("SyncAlt", 0xFFf85e00),
    TRAIN("Train", 0xFF2364aa),
    TWO_WHEELER("TwoWheeler", 0xFFfec601),
    WATER_DROP("WaterDrop", 0xFFe0a4c3),
    WIDGETS("Widgets", 0xFF07a0c3),
    WORK("Work", 0xFF77aca2),;

    companion object {
        fun fromIconName(iconName: String): CategoryIcon? {
            return entries.find { it.iconName == iconName }
        }
    }
}


enum class IncomeCategoryIcon(val iconName: String, val iconColor: Long) : CategoryIcon {
    PAID("Paid", 0xFFf5b700),
    BUSINESS_CENTER("BusinessCenter", 0xFF2364aa),
    TIMELINE("Timeline", 0xFF43291f),
    CARD_GIFT_CARD("CardGiftcard", 0xFF70e000),
    EMOJI_EVENT("EmojiEvents", 0xFF219ebc),
    SYNC_ALT("SyncAlt", 0xFF00a5cf),
    STORE("Store", 0xFFf21b3f),
    SMART_DISPLAY("SmartDisplay", 0xFFfbb02d),;

    companion object {
        fun fromIconName(iconName: String): CategoryIcon? {
            return entries.find { it.iconName == iconName }
        }
    }
}