plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.detekt)
}

android {
    namespace = "com.aidannemeth.weatherly.feature.common.presentation"
    compileSdk = 34

    defaultConfig {
        minSdk = 34
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
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

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        libs.versions.kotlinCompilerExtensionVersion
    }
}

dependencies {
    detektPlugins(libs.detekt)
    implementation(libs.hilt.android)
    implementation(platform(libs.androidx.compose.bom))
    implementation(projects.featureCommon.domain)
}
