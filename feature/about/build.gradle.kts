plugins {
    id("moneyfikasi.android.library")
    id("moneyfikasi.android.compose")
}

android {
    namespace = "dev.muffar.moneyfikasi.about"
}

dependencies {
    implementation(projects.shared.navigation)
    implementation(projects.shared.commonUi)
    implementation(projects.shared.resource)

    implementation(libs.androidx.core.ktx)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
}

