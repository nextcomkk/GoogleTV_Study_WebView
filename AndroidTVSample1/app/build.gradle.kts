plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.androidtv.sample1"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.androidtv.sample1"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.leanback:leanback:1.0.0")

    implementation("com.github.bumptech.glide:glide:4.11.0")

    implementation("com.google.code.gson:gson:2.8.2")

    implementation("androidx.vectordrawable:vectordrawable:1.1.0")
    implementation("androidx.vectordrawable:vectordrawable-animated:1.1.0")

    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.preference:preference:1.2.0")

}