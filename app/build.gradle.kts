plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.alvin.wordapplicationindividualproject"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.alvin.wordapplicationindividualproject"
        minSdk = 28
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Navigation & Fragment
    // implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
     implementation(libs.androidx.navigation.ui.ktx)
    // implementation("androidx.navigation:navigation-fragment:2.7.7")
     implementation(libs.androidx.navigation.fragment)
    // implementation("androidx.fragment:fragment-ktx:1.6.2")
     implementation(libs.androidx.fragment.ktx)


    // implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Coroutine
    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation(libs.kotlinx.coroutines.android)

    // Lifecycle
    // implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Room Database
    // implementation("androidx.room:room-runtime:2.6.1")
    implementation(libs.androidx.room.runtime)
    // annotationProcessor("androidx.room:room-compiler:2.6.1")
    annotationProcessor(libs.androidx.room.compiler)
    // implementation("androidx.room:room-ktx:2.6.1")
    implementation(libs.androidx.room.ktx)
    kapt("androidx.room:room-compiler:2.6.1")

}