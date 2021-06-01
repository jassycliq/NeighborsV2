package com.playbowdogs.neighbors.utils

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class ScopedAppActivity : FragmentActivity(), CoroutineScope {
    protected lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

//    override fun attachBaseContext(base: Context) {
//        super.attachBaseContext(base)
//        // Emulates installation of future on demand modules using SplitCompat.
//        SplitCompat.install(this)
//    }
}
