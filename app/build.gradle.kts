import com.google.protobuf.gradle.id
import java.io.FileNotFoundException
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.protobuf)
}

android {
    namespace = "com.choius323.saisai"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.choius323.saisai"
        minSdk = 27
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    val properties = Properties()
    rootProject.file("local.properties").let { localPropertiesFile ->
        if (localPropertiesFile.exists() && localPropertiesFile.isFile) {
            localPropertiesFile.inputStream().use { input ->
                properties.load(input)
            }
        } else {
            throw FileNotFoundException("Warning: local.properties file not found.")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            val kakaoKey = properties.getProperty("kakao.key")
            buildConfigField("String", "KAKAO_KEY", "\"$kakaoKey\"")
            buildConfigField("String", "SAI_BASE_URL", "\"${properties.getProperty("sai.url")}\"")
        }
        debug {
            val kakaoTestKey = properties.getProperty("kakao.test.key")
            buildConfigField("String", "KAKAO_KEY", "\"$kakaoTestKey\"")
            buildConfigField("String", "SAI_BASE_URL", "\"${properties.getProperty("sai.url")}\"")
        }
        create("releaseTest") {
            initWith(getByName("release"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.3"
    }
    generateProtoTasks {
        all().forEach { tasks ->
            tasks.builtins {
                id("java") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
// Kotlin
    implementation(libs.kotlin.reflect)

    // Androidx Core & Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.lifecycle.compose)
    implementation(libs.androidx.material.icons.extended)

    // Compose Navigation
    implementation(libs.androidx.navigation.compose)

    // Koin
    implementation(libs.koin.android) // Koin for Android
    implementation(libs.koin.compose)  // Koin for Jetpack Compose

    // Ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.android) // Android 엔진
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.auth)
//    implementation(libs.logback.classic)

    // Kotlin Serialization Runtime
    implementation(libs.kotlinx.serialization.json)

    // Image
    implementation(libs.coil.compose)
    implementation(libs.coil.ktor3)
    implementation(libs.coil.svg)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // MVI
    implementation(libs.orbit.core)
    implementation(libs.orbit.compose)
    implementation(libs.orbit.viewmodel)

    // Date
    implementation(libs.threetenabp)

    // Permission
    implementation(libs.accompanist.permissions)

    // Kakao
    implementation(libs.kakao.map)
    implementation(libs.kakao.common)

    // DataStore
    implementation(libs.datastore.preferences)

    // GMS
    implementation(libs.gms.services.location)

    // Proto
    implementation(libs.androidx.datastore.core)
    implementation(libs.protobuf.javalite)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)
    androidTestImplementation(libs.androidx.navigation.testing)
    debugImplementation(libs.orbit.test)
}