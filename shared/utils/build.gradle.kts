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

    testImplementation(libs.junit)
    testImplementation("org.threeten:threetenbp:1.6.8")
}
