object Versions {
    val min_sdk = 24
    val target_sdk = 30
    val compile_sdk = 30

    val kotlin = "1.4.30"
    val android_gradle_plugin = "4.1.3"

    val buildToolsVersion = "30.0.3"
    val cocoapodsext = "0.12"
    val coroutines = "1.4.3-native-mt"
    val kermit = "0.1.8"
    val karmok = "0.1.8"
    val koin = "3.0.1-beta-2"
    val navigation = "2.3.5"
    val ktlint_gradle_plugin = "9.4.1"
    val ktor = "1.5.2"
    val junit = "4.13.1"
    val material = "1.3.0"
    val desugarJdkLibs = "1.1.5"
    val multiplatformSettings = "0.7.4"
    val robolectric = "4.5.1"
    val sqlDelight = "1.4.4"
    val stately = "1.1.4"
    val serialization = "1.1.0"
    val kotlinxDateTime = "0.1.1"
    val turbine = "0.4.1"
    val firebase = "25.13.0"
    val firebaseUI = "7.1.1"
    val calendarView = "2.0.1"
    val threeTen = "1.3.0"
    val moshi = "1.11.0"
    val rootbeer = "0.0.8"
    val playcore = "1.8.1"
    val leakCanary = "2.6"
    val glide = "4.12.0"
    val konfetti = "1.3.2"
    val retrofit = "2.9.0"
    val okhttp = "5.0.0-alpha.2"
    val timber = "4.7.1"
    val loader = "9f766b0"
    val swipe = "1.1.0"
    val orbit = "3.0.1"

    object AndroidX {
        val activity = "1.3.0-alpha06"
        val appcompat = "1.3.0-rc01"
        val constraintlayout = "2.0.4"
        val core = "1.3.2"
        val anim = "1.0.0-alpha02"
        val fragment = "1.3.2"
        val lifecycle = "2.2.0"
        val preferences = "1.1.1"
        val recyclerview = "1.2.0"
        val swipeRefresh = "1.1.0"
        val selection = "1.1.0"
        val test = "1.3.0"
        val test_ext = "1.1.2"
        val viewpager2 = "1.1.0-alpha01"
    }
}

object Deps {
    val android_gradle_plugin = "com.android.tools.build:gradle:${Versions.android_gradle_plugin}"
    val cocoapodsext = "co.touchlab:kotlinnativecocoapods:${Versions.cocoapodsext}"
    val junit = "junit:junit:${Versions.junit}"
    val material = "com.google.android.material:material:${Versions.material}"
    val desugarJdkLibs = "com.android.tools:desugar_jdk_libs:${Versions.desugarJdkLibs}"
    val karmok = "co.touchlab:karmok-library:${Versions.karmok}"
    val kermit = "co.touchlab:kermit:${Versions.kermit}"
    val koinCore = "io.insert-koin:koin-android:${Versions.koin}"
    val koinExt = "io.insert-koin:koin-android-ext:${Versions.koin}"
    val koinWM = "io.insert-koin:koin-androidx-workmanager:${Versions.koin}"
    val multiplatformSettings = "com.russhwolf:multiplatform-settings:${Versions.multiplatformSettings}"
    val multiplatformSettingsTest = "com.russhwolf:multiplatform-settings-test:${Versions.multiplatformSettings}"
    val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    val stately = "co.touchlab:stately-common:${Versions.stately}"
    val kotlinxDateTime = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.kotlinxDateTime}"
    val turbine = "app.cash.turbine:turbine:${Versions.turbine}"

    object AndroidX {
        val appcompat = "androidx.appcompat:appcompat:${Versions.AndroidX.appcompat}"
        val core_ktx = "androidx.core:core-ktx:${Versions.AndroidX.core}"
        val core_anim = "androidx.core:core-animation:${Versions.AndroidX.anim}"
        val constraintlayout = "androidx.constraintlayout:constraintlayout:${Versions.AndroidX.constraintlayout}"
        val recyclerView = "androidx.recyclerview:recyclerview:${Versions.AndroidX.recyclerview}"
        val swipeRefresh = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.AndroidX.swipeRefresh}"
        val selection = "androidx.recyclerview:recyclerview-selection:${Versions.AndroidX.selection}"

        val lifecycle_runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.AndroidX.lifecycle}"
        val lifecycle_viewmodel = "androidx.lifecycle:lifecycle-livedata-core-ktx:${Versions.AndroidX.lifecycle}"
        val lifecycle_viewmodel_extensions = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.AndroidX.lifecycle}"
        val lifecycle_livedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.AndroidX.lifecycle}"
        val lifecycle_extension = "androidx.lifecycle:lifecycle-extensions:${Versions.AndroidX.lifecycle}"

        val activity = "androidx.activity:activity-ktx:${Versions.AndroidX.activity}"
        val fragment =  "androidx.fragment:fragment-ktx:${Versions.AndroidX.fragment}"

        val viewpager2 = "androidx.viewpager2:viewpager2:${Versions.AndroidX.viewpager2}"

        val preferences = "androidx.preference:preference-ktx:${Versions.AndroidX.preferences}"
    }

    object AndroidXTest {
        val core = "androidx.test:core:${Versions.AndroidX.test}"
        val junit = "androidx.test.ext:junit:${Versions.AndroidX.test_ext}"
        val runner = "androidx.test:runner:${Versions.AndroidX.test}"
        val rules = "androidx.test:rules:${Versions.AndroidX.test}"
    }

    object KotlinTest {
        val common = "org.jetbrains.kotlin:kotlin-test-common:${Versions.kotlin}"
        val annotations = "org.jetbrains.kotlin:kotlin-test-annotations-common:${Versions.kotlin}"
        val jvm = "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}"
        val junit = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"
    }

    object Coroutines {
        val common = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
        val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
        val playServices = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.coroutines}"
        val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
    }

    object SqlDelight {
        val gradle = "com.squareup.sqldelight:gradle-plugin:${Versions.sqlDelight}"
        val runtime = "com.squareup.sqldelight:runtime:${Versions.sqlDelight}"
        val coroutinesExtensions = "com.squareup.sqldelight:coroutines-extensions:${Versions.sqlDelight}"
        val runtimeJdk = "com.squareup.sqldelight:runtime-jvm:${Versions.sqlDelight}"
        val driverIos = "com.squareup.sqldelight:native-driver:${Versions.sqlDelight}"
        val driverAndroid = "com.squareup.sqldelight:android-driver:${Versions.sqlDelight}"
    }

    object Ktor {
        val commonCore = "io.ktor:ktor-client-core:${Versions.ktor}"
        val commonJson = "io.ktor:ktor-client-json:${Versions.ktor}"
        val commonLogging = "io.ktor:ktor-client-logging:${Versions.ktor}"
        val androidCore = "io.ktor:ktor-client-cio:${Versions.ktor}"
        val ios = "io.ktor:ktor-client-ios:${Versions.ktor}"
        val commonSerialization = "io.ktor:ktor-client-serialization:${Versions.ktor}"
    }

    object Navigation {
        // Kotlin
        val fragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
        val core = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"

        // Feature module Support
        val feature = "androidx.navigation:navigation-dynamic-features-fragment:${Versions.navigation}"
    }

    object Firebase {
        val ui = "com.firebaseui:firebase-ui-auth:${Versions.firebaseUI}"
        val bom = "com.google.firebase:firebase-bom:${Versions.firebase}"
        const val dynamicLinks = "com.google.firebase:firebase-dynamic-links-ktx"
        const val auth = "com.google.firebase:firebase-auth-ktx"
        const val analytics = "com.google.firebase:firebase-analytics-ktx"
        const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
        const val perf = "com.google.firebase:firebase-perf-ktx"
        const val messaging = "com.google.firebase:firebase-messaging-ktx"
        const val inappmessaging = "com.google.firebase:firebase-inappmessaging-display-ktx"
        const val firestore = "com.google.firebase:firebase-firestore-ktx"
        const val functions = "com.google.firebase:firebase-functions-ktx"
    }

    object Calendar {
        val view = "com.github.prolificinteractive:material-calendarview:${Versions.calendarView}"
        val threeTen = "com.jakewharton.threetenabp:threetenabp:${Versions.threeTen}"
    }

    object Moshi {
        val core = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
        val codeGen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}"
    }

    object Rootbeer {
        val core = "com.scottyab:rootbeer-lib:${Versions.rootbeer}"
    }

    object PlayCore {
        val core = "com.google.android.play:core-ktx:${Versions.playcore}"
    }

    object LeakCanary {
        val core = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"
    }

    object Glide {
        val core = "com.github.bumptech.glide:glide:${Versions.glide}"
        val compiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
    }

    object Konfetti {
        val core = "nl.dionsegijn:konfetti:${Versions.konfetti}"
    }

    object Networking {
        val converter = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
        val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
        val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
        val interceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"
    }

    object Timber {
        val core = "com.jakewharton.timber:timber:${Versions.timber}"
    }

    object Views {
        val loader = "com.github.elye:loaderviewlibrary:${Versions.loader}"
        val animatedRV = ":animatedrecyclerview"
        val swipe = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipe}"
    }

    object Orbit {
        val core = "org.orbit-mvi:orbit-core:${Versions.orbit}"
        val viewModel = "org.orbit-mvi:orbit-viewmodel:${Versions.orbit}"
    }
}