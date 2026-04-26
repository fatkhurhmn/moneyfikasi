plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "dev.muffar.moneyfikasi.resource"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
    }
}