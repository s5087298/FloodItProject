plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

val kotlin_version: String by project
val junit_version: String by project

android {
    compileSdk = 34

    compileOptions {
        sourceCompatibility=JavaVersion.VERSION_17
        targetCompatibility=JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    defaultConfig {
        applicationId = "uk.ac.bmth.aprog.floodit"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        
        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles.add(getDefaultProguardFile("proguard-android-optimize.txt"))
            proguardFiles.add(file("proguard-rules.pro"))
        }
    }
    namespace = "uk.ac.bournemouth.ap.floodit"
}

dependencies {
    implementation(project(":logic"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.vectordrawable)
    implementation(libs.material)
    implementation(libs.matrixlib)

    testImplementation(libs.jupiter.api)
    testImplementation(libs.jupiter.parameters)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
