plugins {
    id("moneyfikasi.android.application")
    id("moneyfikasi.android.compose")
    alias(libs.plugins.ksp)
    alias(libs.plugins.daggerHilt)
}

android {
    namespace = AppConfig.APPLICATION_ID

    defaultConfig {
        applicationId = AppConfig.APPLICATION_ID
        versionCode = AppConfig.VERSION_CODE
        versionName = AppConfig.VERSION_NAME
    }
}

dependencies {

    implementation(projects.shared.domain)
    implementation(projects.shared.data)
    implementation(projects.shared.navigation)
    implementation(projects.shared.commonUi)
    implementation(projects.shared.resource)
    implementation(projects.shared.utils)

    implementation(projects.feature.home)
    implementation(projects.feature.transaction)
    implementation(projects.feature.more)
    implementation(projects.feature.category)
    implementation(projects.feature.wallet)
    implementation(projects.feature.statistic)
    implementation(projects.feature.search)
    implementation(projects.feature.preset)
    implementation(projects.feature.backupRestore)
    implementation(projects.feature.budget)
    implementation(projects.feature.export)
    implementation(projects.feature.applock)
    implementation(projects.feature.settings)
    implementation(projects.feature.recurringTransaction)
    implementation(projects.feature.notification)
    implementation(projects.feature.about)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)

    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)

    implementation(libs.threetenabp)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}
