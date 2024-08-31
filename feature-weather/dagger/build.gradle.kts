plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.detekt)
    alias(libs.plugins.hilt.android)
    kotlin("kapt")
}

android {
    namespace = "com.aidannemeth.weatherly.feature.weather.dagger"
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
}

dependencies {
    detektPlugins(libs.detekt)
    implementation(libs.hilt.android)
    implementation(projects.featureWeather.data)
    implementation(projects.featureWeather.domain)
    implementation(projects.featureWeather.presentation)
    kapt(libs.hilt.android.compiler)

    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)
}
