plugins {
    `kotlin-dsl`
}

group = "dev.muffar.moneyfikasi.buildlogic"

dependencies {
    compileOnly("com.android.tools.build:gradle:8.13.2")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.0")
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "moneyfikasi.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "moneyfikasi.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = "moneyfikasi.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
    }
}

