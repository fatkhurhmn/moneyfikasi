package dev.muffar.moneyfikasi.data.utils

interface CategoryIcon

enum class ExpenseCategoryIcon(val iconName: String) : CategoryIcon {
    APARTMENT("Apartment"),
    BOLT("Bolt"),
    CALL("Call"),
    CAKE("Cake"),
    CHILD_CARE("ChildCare"),
    CHILD_FRIENDLY("ChildFriendly"),
    COFFEE("Coffee"),
    COLLECTIONS_BOOKMARK("CollectionsBookmark"),
    CONFIRMATION_NUMBER("ConfirmationNumber"),
    DIRECTIONS_CAR("DirectionsCar"),
    FAVORITE("Favorite"),
    FITNESS_CENTER("FitnessCenter"),
    FLIGHT("Flight"),
    GAMES("Games"),
    HOME("Home"),
    KITCHEN("Kitchen"),
    LAPTOP("Laptop"),
    LOCAL_BAR("LocalBar"),
    LOCAL_MALL("LocalMall"),
    MEDICAL_SERVICES("MedicalServices"),
    MAP("Map"),
    MOVIE("Movie"),
    PETS("Pets"),
    POOL("Pool"),
    RAMEN_DINING("RamenDining"),
    RESTAURANT("Restaurant"),
    SELF_IMPROVEMENT("SelfImprovement"),
    SHOPPING_CART("ShoppingCart"),
    SMARTPHONE("Smartphone"),
    SPORTS_ESPORTS("SportsEsports"),
    SPORTS_SOCCER("SportsSoccer"),
    STORE("Store"),
    SYNC_ALT("SyncAlt"),
    TRAIN("Train"),
    TWO_WHEELER("TwoWheeler"),
    WATER_DROP("WaterDrop"),
    WIDGETS("Widgets"),
    WORK("Work");
}


enum class IncomeCategoryIcon(val iconName: String) : CategoryIcon {
    PAID("Paid"),
    BUSINESS_CENTER("BusinessCenter"),
    TIMELINE("Timeline"),
    CARD_GIFT_CARD("CardGiftcard"),
    EMOJI_EVENT("EmojiEvents"),
    SYNC_ALT("SyncAlt"),
    STORE("Store"),
    SMART_DISPLAY("SmartDisplay"),
}