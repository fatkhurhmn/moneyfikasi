plugins {
    id("moneyfikasi.android.library")
    alias(libs.plugins.kotlinKapt)
    alias(libs.plugins.daggerHilt)
    id("moneyfikasi.android.compose")
}

android {
    namespace = "dev.muffar.moneyfikasi.budget"
}

dependencies {
    implementation(projects.shared.domain)
    implementation(projects.shared.data)
    implementation(projects.shared.navigation)
    implementation(projects.shared.commonUi)
    implementation(projects.shared.resource)
    implementation(projects.shared.utils)

    implementation(libs.androidx.core.ktx)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
    implementation(libs.hilt.navigation.compose)

    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    implementation(libs.threetenabp)
}

kapt {
    correctErrorTypes = true
}
