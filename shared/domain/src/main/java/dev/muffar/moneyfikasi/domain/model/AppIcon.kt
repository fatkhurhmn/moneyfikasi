package dev.muffar.moneyfikasi.domain.model

enum class AppIcon {
    AccountBalance,
    AccountBalanceWallet,
    Bolt,
    BusinessCenter,
    CardGiftcard,
    Checkroom,
    ChildCare,
    Cloud,
    ConfirmationNumber,
    CreditCard,
    CurrencyBitcoin,
    Devices,
    DirectionsCar,
    EmojiEvents,
    Face,
    Fastfood,
    Favorite,
    FitnessCenter,
    Flight,
    Home,
    Hotel,
    Language,
    LaptopMac,
    LiveTv,
    LocalAtm,
    LocalCafe,
    LocalGasStation,
    LocalGroceryStore,
    LocalPharmacy,
    LocalTaxi,
    Lock,
    Luggage,
    Map,
    MedicalServices,
    MenuBook,
    Movie,
    MusicNote,
    Paid,
    Payments,
    Percent,
    Pets,
    PhoneAndroid,
    PointOfSale,
    Public,
    QrCode,
    RamenDining,
    Receipt,
    ReceiptLong,
    Redeem,
    Replay,
    Restaurant,
    Savings,
    School,
    Security,
    SelfImprovement,
    Sell,
    ShoppingBag,
    SportsEsports,
    Store,
    Subscriptions,
    SyncAlt,
    Train,
    TrendingUp,
    TwoWheeler,
    Verified,
    VideoCameraBack,
    VolunteerActivism,
    Wallet,
    WaterDrop,
    Widgets,
    Wifi;

    companion object {

        fun fromName(name: String): AppIcon {
            return try {
                valueOf(name)
            } catch (_: IllegalArgumentException) {
                Widgets
            }
        }

        val walletIcons = listOf(
            Payments, LocalAtm, Wallet, AccountBalanceWallet, AccountBalance,
            CreditCard, PhoneAndroid, QrCode, Language, Savings, Lock,
            Security, TrendingUp, CurrencyBitcoin, Public, Cloud, Widgets
        ).map { it.name }

        val expenseCategoryIcons = listOf(
            RamenDining, LocalCafe, Restaurant, Fastfood, DirectionsCar,
            TwoWheeler, LocalGasStation, LocalTaxi, Train, Flight,
            LocalGroceryStore, ShoppingBag, Checkroom, Devices, Face,
            ReceiptLong, Bolt, WaterDrop, Wifi, PhoneAndroid, Home,
            MedicalServices, LocalPharmacy, FitnessCenter, SelfImprovement,
            School, MenuBook, ConfirmationNumber, Movie, MusicNote,
            SportsEsports, Favorite, Map, Luggage, Hotel, CardGiftcard,
            VolunteerActivism, Pets, ChildCare, AccountBalance, Security,
            Subscriptions, CreditCard, SyncAlt, Widgets
        ).map { it.name }

        val incomeCategoryIcons = listOf(
            Paid, EmojiEvents, BusinessCenter, LaptopMac, Store,
            TrendingUp, Percent, Verified, Sell, PointOfSale, LiveTv,
            VideoCameraBack, Redeem, Replay, Receipt, SyncAlt, Widgets
        ).map { it.name }
    }
}