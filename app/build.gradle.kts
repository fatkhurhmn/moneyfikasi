plugins {
    id("moneyfikasi.android.application")
    id("moneyfikasi.android.compose")
    alias(libs.plugins.ksp)
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.playPublisher)
}

android {
    namespace = AppConfig.APPLICATION_ID

    defaultConfig {
        applicationId = AppConfig.APPLICATION_ID
        versionCode = AppConfig.VERSION_CODE
        versionName = AppConfig.VERSION_NAME
    }
}

play {
    // Auth priority (handled by GPP):
    // 1. ANDROID_PUBLISHER_CREDENTIALS env var (JSON content) — used in CI
    // 2. serviceAccountCredentials file below — local dev (gitignored)
    val localCredentials = file("play-account.json")
    if (localCredentials.exists()) {
        serviceAccountCredentials.set(localCredentials)
    }
    // Override per-run without editing code, e.g.:
    // ./gradlew publishReleaseBundle -PPLAY_TRACK=production
    // ./gradlew promoteArtifact -PPLAY_FROM_TRACK=internal -PPLAY_PROMOTE_TRACK=production
    track.set(
        providers.gradleProperty("PLAY_TRACK")
            .orElse(providers.environmentVariable("PLAY_TRACK"))
            .orElse("internal")
    )
    releaseStatus.set(
        com.github.triplet.gradle.androidpublisher.ReleaseStatus.COMPLETED
    )
    defaultToAppBundles.set(true)
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
    implementation(projects.feature.splash)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)

    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)
    implementation(libs.androidx.work.runtime)
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
