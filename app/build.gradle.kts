import java.time.*
import java.time.format.DateTimeFormatter

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
    signingConfigs {
        create("release") {
            storeFile = file("/home/jassycliq/Downloads/PlayBow.jks")
            storePassword = "playbow@123"
            keyAlias = "PlayBow"
            keyPassword = "playbow@123"
        }
    }
    compileSdk = 30
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "com.playbowdogs.neighbors"
        minSdk = 24
        targetSdk = 30
        versionCode = getVersionCode()
        versionName = "2.0-${getVersionCode()}"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            multiDexEnabled = true
            isDebuggable = false
            isJniDebuggable = false
            isRenderscriptDebuggable = false
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
    implementation("com.android.support:multidex:2.0.1")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")
    implementation("com.google.android.material:material:1.4.0")
    implementation("com.google.firebase:protolite-well-known-types:18.0.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")
    implementation("androidx.appcompat:appcompat:1.4.0-alpha03")
    implementation("androidx.core:core-ktx:1.7.0-alpha01")
    implementation("androidx.core:core-animation:1.0.0-alpha02")
//    implementation("io.ktor:ktor-client-cio:1.6.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0-beta02")
//    implementation("com.squareup.sqldelight:runtime-jvm:1.5.0")
//    implementation("com.squareup.sqldelight:android-driver:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.5.0")
//    implementation("com.russhwolf:multiplatform-settings:0.7.7")
    implementation("io.insert-koin:koin-android:3.1.2")
    implementation("io.insert-koin:koin-android-ext:3.0.2")
    implementation("io.insert-koin:koin-androidx-workmanager:3.1.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0-alpha02")
    implementation("androidx.lifecycle:lifecycle-livedata-core-ktx:2.4.0-alpha02")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0-alpha02")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0-alpha02")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.activity:activity-ktx:1.3.0-rc01")
    implementation("androidx.fragment:fragment-ktx:1.4.0-alpha04")
    implementation("androidx.preference:preference-ktx:1.1.1")

    implementation("androidx.navigation:navigation-ui-ktx:2.4.0-alpha04")
    implementation("androidx.navigation:navigation-fragment-ktx:2.4.0-alpha04")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.4.0-alpha04")

    implementation(platform("com.google.firebase:firebase-bom:28.2.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-dynamic-links-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-functions-ktx")
    implementation("com.google.firebase:firebase-inappmessaging-display-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-perf-ktx")
    implementation("com.google.firebase:firebase-dynamic-module-support:16.0.0-beta01")

    implementation("com.google.android.gms:play-services-auth:19.0.0")

    implementation("com.github.prolificinteractive:material-calendarview:2.0.1")
    implementation("com.jakewharton.threetenabp:threetenabp:1.3.1")
    implementation("com.squareup.moshi:moshi-kotlin:1.12.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.12.0")
    testImplementation("junit:junit:4.13.2")
//    implementation("com.scottyab:rootbeer-lib:${Versions.rootbeer}")
    implementation("com.google.android.play:core:1.10.0")
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
    implementation("io.github.elye:loaderviewlibrary:3.0.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")
    implementation("androidx.recyclerview:recyclerview-selection:1.2.0-alpha01")
    implementation("com.firebaseui:firebase-ui-auth:7.2.0")
    implementation("org.orbit-mvi:orbit-core:4.0.1")
    implementation("org.orbit-mvi:orbit-viewmodel:4.0.1")
    implementation(kotlin("stdlib-jdk7", "1.5.10"))
}

fun getVersionCode(offset: Int = 0): Int {
    val date = LocalDateTime.now()
    val formattedDate = date.format(DateTimeFormatter.ofPattern("yyMMddHH"))
    return formattedDate.toString().toInt() + offset
}
