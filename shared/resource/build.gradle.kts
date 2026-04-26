plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "dev.muffar.moneyfikasi.resource"
    compileSdk = 37

    defaultConfig {
        minSdk = 26
    }
}