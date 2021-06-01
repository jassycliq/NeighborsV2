// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://maven.google.com")
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://jitpack.io")
    }
    dependencies {
        classpath(Deps.android_gradle_plugin)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.32")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}")
        classpath("com.google.gms:google-services:4.3.8")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.6.1")
        classpath("com.google.firebase:perf-plugin:1.4.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.32")
//        classpath("com.squareup.leakcanary:leakcanary-deobfuscation-gradle-plugin:${Versions.leakCanary}")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}