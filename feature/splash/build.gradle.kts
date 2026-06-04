plugins {
    id("moneyfikasi.android.library")
    id("moneyfikasi.android.compose")
}

android {
    namespace = "dev.muffar.moneyfikasi.splash"
}

dependencies {
    implementation(projects.shared.navigation)
    implementation(projects.shared.resource)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
}
