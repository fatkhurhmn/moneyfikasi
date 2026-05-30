plugins {
    id("moneyfikasi.android.library")
}

android {
    namespace = "dev.muffar.moneyfikasi.utils"
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(libs.threetenabp)

    implementation(projects.shared.resource)
}
