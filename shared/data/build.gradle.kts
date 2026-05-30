plugins {
    id("moneyfikasi.android.library")
    alias(libs.plugins.kotlinKapt)
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "dev.muffar.moneyfikasi.data"
}

dependencies {

    implementation(projects.shared.domain)
    implementation(projects.shared.resource)
    implementation(projects.shared.utils)

    implementation(libs.bundles.room)
    implementation(libs.androidx.documentfile)
    kapt(libs.room.compiler)
    annotationProcessor(libs.room.compiler)

    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    implementation(libs.threetenabp)

    implementation(libs.androidx.datastore)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.hilt.work)
    kapt(libs.androidx.hilt.compiler)

    implementation(libs.androidx.paging.runtime)
}

kapt {
    correctErrorTypes = true
}
