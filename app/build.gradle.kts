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
    compileSdk = Versions.compile_sdk
    buildToolsVersion = Versions.buildToolsVersion

    defaultConfig {
        applicationId = "com.playbowdogs.neighbors"
        minSdk = Versions.min_sdk
        targetSdk = Versions.target_sdk
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

    implementation(Deps.AndroidX.recyclerView)
    implementation(Deps.AndroidX.swipeRefresh)
    implementation(Deps.material)
    coreLibraryDesugaring(Deps.desugarJdkLibs)
    implementation(Deps.AndroidX.appcompat)
    implementation(Deps.AndroidX.core_ktx)
    implementation(Deps.AndroidX.core_anim)
    implementation(Deps.Ktor.androidCore)
    implementation(Deps.AndroidX.constraintlayout)
    implementation(Deps.SqlDelight.runtimeJdk)
    implementation(Deps.SqlDelight.driverAndroid)
    implementation(Deps.Coroutines.common)
    implementation(Deps.Coroutines.android)
    implementation(Deps.Coroutines.playServices)
    implementation(Deps.multiplatformSettings)
    implementation(Deps.koinCore)
    implementation(Deps.koinExt)
    implementation(Deps.koinWM)
    implementation(Deps.AndroidX.lifecycle_runtime)
    implementation(Deps.AndroidX.lifecycle_viewmodel)
    implementation(Deps.AndroidX.lifecycle_viewmodel_extensions)
    implementation(Deps.AndroidX.lifecycle_livedata)
    implementation(Deps.AndroidX.lifecycle_extension)
    implementation(Deps.AndroidX.activity)
    implementation(Deps.AndroidX.fragment)
    implementation(Deps.AndroidX.preferences)

    implementation(Deps.Navigation.core)
    implementation(Deps.Navigation.fragment)
    implementation(Deps.Navigation.feature)
    implementation(platform(Deps.Firebase.bom))
    implementation(Deps.Firebase.analytics)
    implementation(Deps.Firebase.auth)
    implementation(Deps.Firebase.crashlytics)
    implementation(Deps.Firebase.dynamicLinks)
    implementation(Deps.Firebase.firestore)
    implementation(Deps.Firebase.functions)
    implementation(Deps.Firebase.inappmessaging)
    implementation(Deps.Firebase.messaging)
    implementation(Deps.Firebase.perf)
    implementation(Deps.Calendar.view)
    implementation(Deps.Calendar.threeTen)
    implementation(Deps.Moshi.core)
    kapt(Deps.Moshi.codeGen)
    testImplementation(Deps.junit)
    implementation(Deps.Rootbeer.core)
    implementation(Deps.PlayCore.core)
//    implementation(Deps.LeakCanary.core)
    implementation(Deps.Glide.core)
    kapt(Deps.Glide.compiler)
    implementation(Deps.Konfetti.core)
    implementation(Deps.Networking.retrofit)
    implementation(Deps.Networking.converter)
    implementation(Deps.Networking.okhttp)
    implementation(Deps.Networking.interceptor)
    implementation(Deps.Timber.core)
    implementation(project(path = Deps.Views.animatedRV))
    implementation(Deps.Views.loader)
    implementation(Deps.Views.swipe)
    implementation(Deps.AndroidX.selection)
    implementation(Deps.Firebase.ui)
    implementation(Deps.Orbit.core)
    implementation(Deps.Orbit.viewModel)
}
