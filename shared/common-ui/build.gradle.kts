import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "dev.muffar.moneyfikasi.common_ui"
    compileSdk = 37

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {

    implementation(projects.shared.resource)
    implementation(projects.shared.domain)
    implementation(projects.shared.utils)

    implementation(libs.androidx.core.ktx)

    implementation(libs.bundles.compose)
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.threetenabp)

    debugImplementation(libs.androidx.ui.tooling)

    implementation(libs.mp.android.chart)

    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
}