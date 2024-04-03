plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "dev.muffar.moneyfikasi.resource"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }
}