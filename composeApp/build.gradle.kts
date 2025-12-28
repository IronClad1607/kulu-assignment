import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.buildConfig)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        val konan = iosTarget.konanTarget.name
        val slice = when (konan) {
            "ios_arm64" -> "ios-arm64"
            "ios_x64", "ios_simulator_arm64" -> "ios-arm64_x86_64-simulator"
            else -> error("Unknown iOS target: $konan")
        }

        iosTarget.compilations.getByName("main").compilerOptions.configure {
            freeCompilerArgs.add("-Xsystem-framework=Network")
        }

        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true

            // Keep the export line for the framework's visibility
            export("platform.Network:Network")

            // CORRECT WAY to add linker flags 🛠️
            linkerOpts.add("-framework")
            linkerOpts.add("Network")
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.ktor.client.okhttp)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.sqldelight.driver.android)

            api(libs.koin.android)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.kermit)
            implementation(libs.cmptoast)

            api(libs.koin.core)
            api(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel.navigation)
            implementation(libs.navigation.compose)
            implementation(libs.sqldelight.coroutines)

            implementation(libs.kotlinx.serialization.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.auth)
            implementation(libs.kotlinx.coroutines.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        val commonMain by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        val iosMain = findByName("iosMain") ?: create("iosMain").apply {
            dependsOn(commonMain)
        }

        // hook the platform-specific mains up to iosMain:
        iosX64Main.dependsOn(iosMain)
        iosArm64Main.dependsOn(iosMain)
        iosSimulatorArm64Main.dependsOn(iosMain)

        // finally, add your Darwin client into iosMain:
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sqldelight.driver.native)
        }
    }
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

buildConfig {
    packageName("com.ishaan.kuluassignment")

    val apiKey = localProperties.getProperty("apiKey")
    buildConfigField("String", "TMDB_API_KEY", "\"$apiKey\"")
}

sqldelight {
    databases {
        create("MovieDatabase") {
            packageName.set("com.ishaan.kuluassignment.db")
        }
    }
}

android {
    namespace = "com.ishaan.kuluassignment"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.ishaan.kuluassignment"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

