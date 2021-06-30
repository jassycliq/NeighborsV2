plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
    id("org.jetbrains.kotlin.plugin.parcelize")
//    id("com.squareup.leakcanary.deobfuscation")
}

android {
    compileSdk = 30
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "com.playbowdogs.neighbors"
        minSdk = 24
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.google.android.material:material:1.3.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("androidx.core:core-ktx:1.5.0")
    implementation("androidx.core:core-animation:1.0.0-alpha02")
//    implementation("io.ktor:ktor-client-cio:1.6.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0-beta02")
//    implementation("com.squareup.sqldelight:runtime-jvm:1.5.0")
//    implementation("com.squareup.sqldelight:android-driver:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0-native-mt")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0-native-mt")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.5.0-native-mt")
//    implementation("com.russhwolf:multiplatform-settings:0.7.7")
    implementation("io.insert-koin:koin-android:3.1.0")
    implementation("io.insert-koin:koin-android-ext:3.0.2")
    implementation("io.insert-koin:koin-androidx-workmanager:3.1.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-livedata-core-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.activity:activity-ktx:1.2.3")
    implementation("androidx.fragment:fragment-ktx:1.3.5")
    implementation("androidx.preference:preference-ktx:1.1.1")

    implementation("androidx.navigation:navigation-ui-ktx:2.3.5")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.3.5")

    implementation(platform("com.google.firebase:firebase-bom:28.1.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx:21.0.1")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-dynamic-links-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-functions-ktx")
    implementation("com.google.firebase:firebase-inappmessaging-display-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-perf-ktx")
    implementation("com.google.firebase:firebase-dynamic-module-support:16.0.0-beta01")

    implementation("com.github.prolificinteractive:material-calendarview:2.0.1")
    implementation("com.jakewharton.threetenabp:threetenabp:1.3.1")
    implementation("com.squareup.moshi:moshi-kotlin:1.12.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.12.0")
    testImplementation("junit:junit:4.13.2")
//    implementation("com.scottyab:rootbeer-lib:${Versions.rootbeer}")
    implementation("com.google.android.play:core-ktx:1.8.1")
//    implementation(Deps.LeakCanary.core)
    implementation("com.github.bumptech.glide:glide:4.12.0")
    kapt("com.github.bumptech.glide:compiler:4.12.0")
//    implementation("nl.dionsegijn:konfetti:1.3.2")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")
    implementation("com.jakewharton.timber:timber:4.7.1")
    implementation(project(path = ":animatedrecyclerview"))
    implementation("com.github.elye:loaderviewlibrary:9f766b0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.recyclerview:recyclerview-selection:1.1.0")
    implementation("com.firebaseui:firebase-ui-auth:7.2.0")
    implementation("org.orbit-mvi:orbit-core:3.1.1")
    implementation("org.orbit-mvi:orbit-viewmodel:3.1.1")
//    implementation(kotlinModule("stdlib-jdk7", kotlin_version))
}
