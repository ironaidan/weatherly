plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.detekt)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.aidannemeth.weatherly.feature.common.dagger"
    compileSdk = 35

    defaultConfig {
        minSdk = 34
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    detektPlugins(libs.detekt)
    implementation(libs.hilt.android)
    implementation(projects.featureCommon.data)
    implementation(projects.featureCommon.domain)
    implementation(projects.featureCommon.presentation)
    ksp(libs.hilt.android.compiler)
}
