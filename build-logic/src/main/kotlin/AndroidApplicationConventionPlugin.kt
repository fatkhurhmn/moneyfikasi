import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import java.io.File
import java.util.Properties

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.android.application")

        extensions.configure<BasePluginExtension> {
            archivesName.set(AppConfig.ARCHIVE_BASE_NAME)
        }

        extensions.configure<ApplicationExtension> {
            compileSdk = AppConfig.COMPILE_SDK

            buildFeatures {
                resValues = true
            }

            defaultConfig {
                minSdk = AppConfig.MIN_SDK
                targetSdk = AppConfig.TARGET_SDK
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                vectorDrawables {
                    useSupportLibrary = true
                }
            }

            signingConfigs {
                val localProperties = loadLocalProperties()
                val releaseStoreFile = signingProperty(AppConfig.RELEASE_STORE_FILE_PROPERTY, localProperties)
                val releaseStorePassword = signingProperty(AppConfig.RELEASE_STORE_PASSWORD_PROPERTY, localProperties)
                val releaseKeyAlias = signingProperty(AppConfig.RELEASE_KEY_ALIAS_PROPERTY, localProperties)
                val releaseKeyPassword = signingProperty(AppConfig.RELEASE_KEY_PASSWORD_PROPERTY, localProperties)

                if (
                    releaseStoreFile.isPresent &&
                    releaseStorePassword.isPresent &&
                    releaseKeyAlias.isPresent &&
                    releaseKeyPassword.isPresent
                ) {
                    create("release") {
                        storeFile = file(releaseStoreFile.get())
                        storePassword = releaseStorePassword.get()
                        keyAlias = releaseKeyAlias.get()
                        keyPassword = releaseKeyPassword.get()
                    }
                }
            }

            buildTypes {
                debug {
                    applicationIdSuffix = AppConfig.DEBUG_APPLICATION_ID_SUFFIX
                    versionNameSuffix = AppConfig.DEBUG_VERSION_NAME_SUFFIX
                }

                release {
                    signingConfigs.findByName("release")?.let {
                        signingConfig = it
                    }
                    isMinifyEnabled = false
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }

            packaging {
                resources {
                    excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    excludes += "/META-INF/DEPENDENCIES"
                    excludes += "/META-INF/LICENSE*"
                    excludes += "/META-INF/NOTICE*"
                }
            }
        }

        extensions.configure<KotlinAndroidProjectExtension> {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }
    }

    private fun Project.signingProperty(name: String, localProperties: Properties): Provider<String> =
        providers.gradleProperty(name)
            .orElse(providers.environmentVariable(name))
            .orElse(providers.provider { localProperties.getProperty(name) })
            .map(String::trim)
            .map { value ->
                if (name == AppConfig.RELEASE_STORE_FILE_PROPERTY) {
                    File(value).takeIf(File::isAbsolute)?.path ?: rootProject.layout.projectDirectory.file(value).asFile.path
                } else {
                    value
                }
            }

    private fun Project.loadLocalProperties(): Properties {
        val properties = Properties()
        val propertiesFile = rootProject.file("local.properties")
        if (propertiesFile.isFile) {
            propertiesFile.inputStream().use(properties::load)
        }
        return properties
    }
}
