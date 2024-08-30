plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.detekt)
}

android {
    namespace = "com.aidannemeth.weatherly.feature.weather.data"
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

    packaging {
        resources {
            excludes += "META-INF/LICENSE*"
        }
    }
}

dependencies {
    detektPlugins(libs.detekt)
    implementation(libs.arrow.core)
    implementation(libs.arrow.fx.coroutines)
    implementation(libs.hilt.android)
    implementation(libs.kotlin.coroutines)
    implementation(libs.room)
    implementation(platform(libs.arrow.bom))
    implementation(projects.featureCommon.domain)
    implementation(projects.featureWeather.domain)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.mockk.android)
    testImplementation(kotlin("test"))
    testImplementation(libs.junit)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
}
