plugins {
    id("moneyfikasi.android.library")
    id("moneyfikasi.android.compose")
}

android {
    namespace = "dev.muffar.moneyfikasi.navigation"
}

dependencies {
    implementation(projects.shared.resource)
    implementation(projects.shared.commonUi)
    implementation(projects.shared.domain)

    implementation(libs.bundles.compose)
    implementation(platform(libs.androidx.compose.bom))

    debugImplementation(libs.androidx.ui.tooling)
}
