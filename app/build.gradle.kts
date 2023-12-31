plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.performafuboru"
    compileSdk = 33
    defaultConfig {
        applicationId = "com.example.performafuboru"
        minSdk = 23
        targetSdk = 33
        versionCode = 1
        versionName = "1.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-analytics:21.2.0")
    implementation("com.google.firebase:firebase-database:20.1.0")
    implementation("com.google.firebase:firebase-storage:20.1.0")
    implementation("com.firebaseui:firebase-ui-database:2.0.0")
    implementation("com.google.firebase:firebase-auth:19.4.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.github.VishnuSivadasVS:Advanced-HttpURLConnection:1.2")
    implementation("com.amitshekhar.android:jackson-android-networking:1.0.2")
    implementation("com.android.volley:volley:1.1.1")
    implementation("com.karumi:dexter:6.2.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}