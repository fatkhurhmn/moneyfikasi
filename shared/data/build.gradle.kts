plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlinKapt)
    alias(libs.plugins.daggerHilt)
}

android {
    namespace = "dev.muffar.moneyfikasi.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.bundles.room)
    kapt(libs.room.compiler)
    annotationProcessor(libs.room.compiler)

    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
}

kapt {
    correctErrorTypes = true
}