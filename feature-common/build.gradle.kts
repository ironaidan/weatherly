plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.aidannemeth.weatherly.feature.common"
    compileSdk = 35

    defaultConfig {
        minSdk = 34
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    api(projects.featureCommon.dagger)
    api(projects.featureCommon.data)
    api(projects.featureCommon.domain)
    api(projects.featureCommon.presentation)
}
