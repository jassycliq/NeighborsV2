package com.playbowdogs.neighbors

import com.google.android.play.core.splitcompat.SplitCompatApplication
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.playbowdogs.neighbors.di.*
import com.playbowdogs.neighbors.utils.DefaultCurrentActivityListener
import com.playbowdogs.neighbors.utils.GlideApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree


@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@FlowPreview
class NeighborAndroid : SplitCompatApplication() {
    private val defaultCurrentActivityListener: DefaultCurrentActivityListener by inject()
    private lateinit var mFirebaseAnalytics: FirebaseAnalytics

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@NeighborAndroid)
            modules(
                listOf(
                    appModule,
                    coroutineScopeModule,
                    sharedPrefModule,
                    networkModule,
                    angelCamModule,
//                    playbowModule,
                    firebaseAuthModule,
                    firestoreModule,
                    firebaseFunctionsModule,
                    firebaseUIViewModelModule,
                    acuityModule,
                    onBoardingModule,
                    )
            )
        }
        registerActivityLifecycleCallbacks(defaultCurrentActivityListener)

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }

        // Start analytics
        if (BuildConfig.DEBUG.not()) {
            mFirebaseAnalytics = Firebase.analytics
        }
    }

    override fun onTrimMemory(level: Int) {
        GlideApp.with(applicationContext).onTrimMemory(TRIM_MEMORY_MODERATE)
        super.onTrimMemory(level)
    }
}
