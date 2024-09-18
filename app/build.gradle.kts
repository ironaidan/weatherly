plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.detekt)
    alias(libs.plugins.hilt.android)
    kotlin("kapt")
    kotlin("plugin.serialization")
}

android {
    namespace = "com.aidannemeth.weatherly"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.aidannemeth.weatherly"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "com.aidannemeth.weatherly.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        buildConfig = true
    }

    composeOptions {
        libs.versions.kotlinCompilerExtensionVersion
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation(libs.androidx.ui.tooling)
    detektPlugins(libs.detekt)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.app.startup)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.arrow.core)
    implementation(libs.arrow.core.retrofit)
    implementation(libs.arrow.fx.coroutines)
    implementation(libs.hilt.android)
    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.room)
    implementation(libs.room.ktx)
    implementation(libs.timber)
    implementation(platform(libs.androidx.compose.bom))
    implementation(platform(libs.arrow.bom))
    implementation(projects.featureCommon)
    implementation(projects.featureWeather)
    kapt(libs.hilt.android.compiler)
    kapt(libs.room.compiler)

    androidTestImplementation(kotlin("test"))
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.okhttp.mock.webserver)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    kaptAndroidTest(libs.hilt.android.compiler)
    testImplementation(kotlin("test"))
    testImplementation(libs.junit)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.okhttp.mock.webserver)
    testImplementation(libs.room.testing)
}
