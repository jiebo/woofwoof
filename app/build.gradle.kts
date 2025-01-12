plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.tijiebo.woofwoof"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tijiebo.woofwoof"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources.excludes.add("META-INF/licenses/**")
        resources.excludes.add("META-INF/LICENSE.md")
        resources.excludes.add("META-INF/LICENSE-notice.md")
        resources.excludes.add("META-INF/NOTICE.md")
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
        resources.excludes.add("META-INF/DEPENDENCIES")
    }
}

dependencies {
    // ==================== Compose BOM ====================
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)

    // ==================== Coil ====================
    implementation(libs.coil.compose)
    implementation(libs.coil.network)

    // ==================== Lottie ====================
    implementation(libs.lottie.compose)

    // ==================== Android ====================
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // ==================== Dependency Injection ====================
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // ==================== Navigation ====================
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.core)

    // ==================== Networking ====================
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    ksp(libs.moshi.kotlin.codegen)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // ==================== Logging ====================
    implementation(libs.timber)

    // ==================== Testing ====================
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.shared.preferences.mock)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.androidx.ui.test)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)
    androidTestImplementation(platform(libs.androidx.compose.bom))
}
