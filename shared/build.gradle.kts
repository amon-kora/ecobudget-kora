plugins {
    kotlin("multiplatform")
    id("com.android.library")
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.jetbrains.compose)
    kotlin("plugin.serialization") version "2.0.21"

}

kotlin {
    // Cible Android
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    // Cibles iOS avec génération du framework binaire
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    // Dépendances communes
    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel:2.8.0")
            api(compose.components.resources)
            implementation(compose.runtime)
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
            // Ktor Core & Négociation JSON
            implementation("io.ktor:ktor-client-core:2.3.12")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
        }

        androidMain.dependencies {
            // Moteur réseau pour Android
            implementation("io.ktor:ktor-client-okhttp:2.3.12")
        }

        iosMain.dependencies {
            // Moteur réseau pour iOS
            implementation("io.ktor:ktor-client-darwin:2.3.12")
        }
    }
}

// Configuration minimale requise par le plugin com.android.library
android {
    namespace = "com.example.shared"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

compose {
    resources {
        publicResClass = true
    }
}