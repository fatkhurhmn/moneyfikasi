object AppConfig {
    const val APPLICATION_ID = "dev.muffar.moneyfikasi"
    const val APP_NAME = "moneyfikasi"

    const val COMPILE_SDK = 37
    const val MIN_SDK = 26
    const val TARGET_SDK = 36

    private const val VERSION_MAJOR = 0
    private const val VERSION_MINOR = 1
    private const val VERSION_PATCH = 0

    const val VERSION_CODE = 1
    const val VERSION_NAME = "$VERSION_MAJOR.$VERSION_MINOR.$VERSION_PATCH"

    const val ARCHIVE_BASE_NAME = "$APP_NAME-v$VERSION_NAME-$VERSION_CODE"

    const val DEBUG_APPLICATION_ID_SUFFIX = ".debug"
    const val DEBUG_VERSION_NAME_SUFFIX = "-debug"

    const val RELEASE_STORE_FILE_PROPERTY = "MONEYFIKASI_RELEASE_STORE_FILE"
    const val RELEASE_STORE_PASSWORD_PROPERTY = "MONEYFIKASI_RELEASE_STORE_PASSWORD"
    const val RELEASE_KEY_ALIAS_PROPERTY = "MONEYFIKASI_RELEASE_KEY_ALIAS"
    const val RELEASE_KEY_PASSWORD_PROPERTY = "MONEYFIKASI_RELEASE_KEY_PASSWORD"
}
