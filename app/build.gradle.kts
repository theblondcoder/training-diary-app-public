plugins {
    alias(libs.plugins.android.application)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.training_diary_app"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.training_diary_app"
        minSdk = 24
        targetSdk = 36
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
    //room
    val room_version = "2.8.4"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    //lifecycle
    val lifecycle_version = "2.10.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    //navigation
    val nav_version = "2.9.8"
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")
    //recycler
    val recycler_version="1.4.0"
    implementation("androidx.recyclerview:recyclerview:$recycler_version")
    //google components
    val materialVersion="1.14.0"
    implementation("com.google.android.material:material:$materialVersion")
}