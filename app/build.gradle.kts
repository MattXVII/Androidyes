import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

val localProperties = Properties().apply {
    rootProject.file("local.properties")
        .takeIf { it.exists() }
        ?.inputStream()
        ?.use(::load)
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.ksp)
    alias(libs.plugins.serialization)
    alias(libs.plugins.hilt)
}

android {
    namespace = "it.univaq.speedcamerafinder"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "it.univaq.speedcamerafinder"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        manifestPlaceholders["MAPS_API_KEY"] =
            localProperties.getProperty("MAPS_API_KEY", "")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.57.2")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    ksp("com.google.dagger:hilt-android-compiler:2.57.2")

    // Room
    implementation("androidx.room:room-runtime:2.8.4")
    implementation("androidx.room:room-ktx:2.8.4")
    ksp("androidx.room:room-compiler:2.8.4")

    // Nav3
    implementation("androidx.navigation3:navigation3-runtime:1.0.0")
    implementation("androidx.navigation3:navigation3-ui:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

    // Google Maps
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.maps.android:maps-compose:6.12.2")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.accompanist:accompanist-permissions:0.37.3")

    // Icons
    implementation("androidx.compose.material:material-icons-extended:1.7.8")


}
