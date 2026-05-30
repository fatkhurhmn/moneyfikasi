plugins {
    id("moneyfikasi.android.library")
    id("moneyfikasi.android.compose")
}

android {
    namespace = "dev.muffar.moneyfikasi.common_ui"
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
