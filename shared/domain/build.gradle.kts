plugins {
    id("moneyfikasi.android.library")
    alias(libs.plugins.ksp)
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "dev.muffar.moneyfikasi.domain"
}

dependencies {

    implementation(projects.shared.utils)
    implementation(projects.shared.resource)

    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)

    implementation(libs.threetenabp)

    implementation(libs.androidx.paging.common)

    implementation(libs.kotlinx.serialization.json)
}
