package dev.muffar.moneyfikasi.common_ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.automirrored.rounded.ReceiptLong
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.BusinessCenter
import androidx.compose.material.icons.rounded.CardGiftcard
import androidx.compose.material.icons.rounded.Checkroom
import androidx.compose.material.icons.rounded.ChildCare
import androidx.compose.material.icons.rounded.Cloud
import androidx.compose.material.icons.rounded.ConfirmationNumber
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.CurrencyBitcoin
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Flight
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Hotel
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.LaptopMac
import androidx.compose.material.icons.rounded.LiveTv
import androidx.compose.material.icons.rounded.LocalAtm
import androidx.compose.material.icons.rounded.LocalCafe
import androidx.compose.material.icons.rounded.LocalGasStation
import androidx.compose.material.icons.rounded.LocalGroceryStore
import androidx.compose.material.icons.rounded.LocalPharmacy
import androidx.compose.material.icons.rounded.LocalTaxi
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Luggage
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Paid
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.Percent
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.material.icons.rounded.PointOfSale
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.QrCode
import androidx.compose.material.icons.rounded.RamenDining
import androidx.compose.material.icons.rounded.Receipt
import androidx.compose.material.icons.rounded.Redeem
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.SelfImprovement
import androidx.compose.material.icons.rounded.Sell
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material.icons.rounded.SportsEsports
import androidx.compose.material.icons.rounded.Store
import androidx.compose.material.icons.rounded.Subscriptions
import androidx.compose.material.icons.rounded.SyncAlt
import androidx.compose.material.icons.rounded.Train
import androidx.compose.material.icons.rounded.TwoWheeler
import androidx.compose.material.icons.rounded.Verified
import androidx.compose.material.icons.rounded.VideoCameraBack
import androidx.compose.material.icons.rounded.VolunteerActivism
import androidx.compose.material.icons.rounded.Wallet
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material.icons.rounded.Widgets
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.ui.graphics.vector.ImageVector
import dev.muffar.moneyfikasi.domain.model.AppIcon

object IconMapper {
    fun AppIcon.toImageVector(): ImageVector {
        return when (this) {
            AppIcon.AccountBalance -> Icons.Rounded.AccountBalance
            AppIcon.AccountBalanceWallet -> Icons.Rounded.AccountBalanceWallet
            AppIcon.Bolt -> Icons.Rounded.Bolt
            AppIcon.BusinessCenter -> Icons.Rounded.BusinessCenter
            AppIcon.CardGiftcard -> Icons.Rounded.CardGiftcard
            AppIcon.Checkroom -> Icons.Rounded.Checkroom
            AppIcon.ChildCare -> Icons.Rounded.ChildCare
            AppIcon.Cloud -> Icons.Rounded.Cloud
            AppIcon.ConfirmationNumber -> Icons.Rounded.ConfirmationNumber
            AppIcon.CreditCard -> Icons.Rounded.CreditCard
            AppIcon.CurrencyBitcoin -> Icons.Rounded.CurrencyBitcoin
            AppIcon.Devices -> Icons.Rounded.Devices
            AppIcon.DirectionsCar -> Icons.Rounded.DirectionsCar
            AppIcon.EmojiEvents -> Icons.Rounded.EmojiEvents
            AppIcon.Face -> Icons.Rounded.Face
            AppIcon.Fastfood -> Icons.Rounded.Fastfood
            AppIcon.Favorite -> Icons.Rounded.Favorite
            AppIcon.FitnessCenter -> Icons.Rounded.FitnessCenter
            AppIcon.Flight -> Icons.Rounded.Flight
            AppIcon.Home -> Icons.Rounded.Home
            AppIcon.Hotel -> Icons.Rounded.Hotel
            AppIcon.Language -> Icons.Rounded.Language
            AppIcon.LaptopMac -> Icons.Rounded.LaptopMac
            AppIcon.LiveTv -> Icons.Rounded.LiveTv
            AppIcon.LocalAtm -> Icons.Rounded.LocalAtm
            AppIcon.LocalCafe -> Icons.Rounded.LocalCafe
            AppIcon.LocalGasStation -> Icons.Rounded.LocalGasStation
            AppIcon.LocalGroceryStore -> Icons.Rounded.LocalGroceryStore
            AppIcon.LocalPharmacy -> Icons.Rounded.LocalPharmacy
            AppIcon.LocalTaxi -> Icons.Rounded.LocalTaxi
            AppIcon.Lock -> Icons.Rounded.Lock
            AppIcon.Luggage -> Icons.Rounded.Luggage
            AppIcon.Map -> Icons.Rounded.Map
            AppIcon.MedicalServices -> Icons.Rounded.MedicalServices
            AppIcon.MenuBook -> Icons.AutoMirrored.Rounded.MenuBook
            AppIcon.Movie -> Icons.Rounded.Movie
            AppIcon.MusicNote -> Icons.Rounded.MusicNote
            AppIcon.Paid -> Icons.Rounded.Paid
            AppIcon.Payments -> Icons.Rounded.Payments
            AppIcon.Percent -> Icons.Rounded.Percent
            AppIcon.Pets -> Icons.Rounded.Pets
            AppIcon.PhoneAndroid -> Icons.Rounded.PhoneAndroid
            AppIcon.PointOfSale -> Icons.Rounded.PointOfSale
            AppIcon.Public -> Icons.Rounded.Public
            AppIcon.QrCode -> Icons.Rounded.QrCode
            AppIcon.RamenDining -> Icons.Rounded.RamenDining
            AppIcon.Receipt -> Icons.Rounded.Receipt
            AppIcon.ReceiptLong -> Icons.AutoMirrored.Rounded.ReceiptLong
            AppIcon.Redeem -> Icons.Rounded.Redeem
            AppIcon.Replay -> Icons.Rounded.Replay
            AppIcon.Restaurant -> Icons.Rounded.Restaurant
            AppIcon.Savings -> Icons.Rounded.Savings
            AppIcon.School -> Icons.Rounded.School
            AppIcon.Security -> Icons.Rounded.Security
            AppIcon.SelfImprovement -> Icons.Rounded.SelfImprovement
            AppIcon.Sell -> Icons.Rounded.Sell
            AppIcon.ShoppingBag -> Icons.Rounded.ShoppingBag
            AppIcon.SportsEsports -> Icons.Rounded.SportsEsports
            AppIcon.Store -> Icons.Rounded.Store
            AppIcon.Subscriptions -> Icons.Rounded.Subscriptions
            AppIcon.SyncAlt -> Icons.Rounded.SyncAlt
            AppIcon.Train -> Icons.Rounded.Train
            AppIcon.TrendingUp -> Icons.AutoMirrored.Rounded.TrendingUp
            AppIcon.TwoWheeler -> Icons.Rounded.TwoWheeler
            AppIcon.Verified -> Icons.Rounded.Verified
            AppIcon.VideoCameraBack -> Icons.Rounded.VideoCameraBack
            AppIcon.VolunteerActivism -> Icons.Rounded.VolunteerActivism
            AppIcon.Wallet -> Icons.Rounded.Wallet
            AppIcon.WaterDrop -> Icons.Rounded.WaterDrop
            AppIcon.Widgets -> Icons.Rounded.Widgets
            AppIcon.Wifi -> Icons.Rounded.Wifi
        }
    }
}