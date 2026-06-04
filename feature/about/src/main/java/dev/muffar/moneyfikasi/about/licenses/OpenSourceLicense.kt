package dev.muffar.moneyfikasi.about.licenses

data class OpenSourceLicense(
    val name: String,
    val artifacts: String,
    val licenseName: String,
    val url: String,
)

val openSourceLicenses = listOf(
    OpenSourceLicense(
        name = "AndroidX",
        artifacts = "Core KTX, AppCompat, Activity, Lifecycle Runtime, Navigation, Room, DataStore, WorkManager, Paging, Biometric, DocumentFile, Hilt extensions",
        licenseName = "Apache License 2.0",
        url = "https://developer.android.com/jetpack/androidx"
    ),
    OpenSourceLicense(
        name = "Jetpack Compose",
        artifacts = "Compose UI, UI Graphics, Material 3, Material Icons Extended, Navigation Compose",
        licenseName = "Apache License 2.0",
        url = "https://developer.android.com/jetpack/compose"
    ),
    OpenSourceLicense(
        name = "Kotlin and Kotlinx Serialization",
        artifacts = "Kotlin standard libraries, kotlinx-serialization-json",
        licenseName = "Apache License 2.0",
        url = "https://kotlinlang.org"
    ),
    OpenSourceLicense(
        name = "Dagger Hilt",
        artifacts = "hilt-android",
        licenseName = "Apache License 2.0",
        url = "https://dagger.dev/hilt"
    ),
    OpenSourceLicense(
        name = "ThreeTenABP",
        artifacts = "com.jakewharton.threetenabp:threetenabp",
        licenseName = "Apache License 2.0",
        url = "https://github.com/JakeWharton/ThreeTenABP"
    ),
    OpenSourceLicense(
        name = "MPAndroidChart",
        artifacts = "com.github.PhilJay:MPAndroidChart",
        licenseName = "Apache License 2.0",
        url = "https://github.com/PhilJay/MPAndroidChart"
    ),
    OpenSourceLicense(
        name = "Compose Shimmer",
        artifacts = "com.valentinilk.shimmer:compose-shimmer",
        licenseName = "Apache License 2.0",
        url = "https://github.com/valentinilk/compose-shimmer"
    ),
    OpenSourceLicense(
        name = "Capturable",
        artifacts = "dev.shreyaspatil:capturable",
        licenseName = "Apache License 2.0",
        url = "https://github.com/PatilShreyas/Capturable"
    ),
    OpenSourceLicense(
        name = "Flaticon Icons",
        artifacts = "Graph, Home, Setting, and Invoice icons designed by HideMaru from Flaticon",
        licenseName = "Flaticon",
        url = "https://www.flaticon.com/authors/hidemaru"
    )
)
