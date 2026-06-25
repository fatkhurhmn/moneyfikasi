import java.util.Properties

plugins {
    id("moneyfikasi.android.library")
    alias(libs.plugins.ksp)
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "dev.muffar.moneyfikasi.data"

    val localProperties = Properties().apply {
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use { load(it) }
        }
    }
    val geminiApiKey = localProperties.getProperty("GEMINI_API_KEY") ?: ""

    defaultConfig {
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
        buildConfigField("String", "GEMINI_API_KEY", "\"$geminiApiKey\"")
    }

    buildFeatures {
        buildConfig = true
    }

    sourceSets {
        getByName("androidTest").assets.directories.add("$projectDir/schemas")
    }
}

dependencies {

    implementation(projects.shared.domain)
    implementation(projects.shared.resource)
    implementation(projects.shared.utils)

    implementation(libs.bundles.room)
    implementation(libs.androidx.documentfile)
    ksp(libs.room.compiler)

    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)

    implementation(libs.threetenabp)

    implementation(libs.androidx.datastore)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)

    implementation(libs.androidx.paging.runtime)
    implementation(libs.google.generative.ai)
}
