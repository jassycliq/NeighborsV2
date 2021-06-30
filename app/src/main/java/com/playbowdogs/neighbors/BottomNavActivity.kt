package com.playbowdogs.neighbors

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.forEach
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.play.core.splitcompat.SplitCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.playbowdogs.neighbors.di.calendarModule
import com.playbowdogs.neighbors.di.liveViewModule
import com.playbowdogs.neighbors.di.recordedClipsListModule
import com.playbowdogs.neighbors.di.settingsModule
import com.playbowdogs.neighbors.firebase.auth.FirebaseAuthResultContract
import com.playbowdogs.neighbors.intent.FirebaseUIState
import com.playbowdogs.neighbors.ui.userType.UserTypeFragment
import com.playbowdogs.neighbors.viewmodel.firebaseUI.NewFirebaseUIViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import timber.log.Timber

@FlowPreview
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class BottomNavActivity : AppCompatActivity() {
    private var currentNavController: LiveData<NavController>? = null

    private val newFVM: NewFirebaseUIViewModel by viewModel()

    private val sharedPref by inject<SharedPreferences>()
    private val sharedPrefEdit by inject<SharedPreferences.Editor>()

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navView: BottomNavigationView
//    private lateinit var konfetti: KonfettiView
    private lateinit var fragmentContainer: FragmentContainerView

    private var job: Deferred<Unit>? = null
    private var konfettiCounter: Int = 0
    private var startMillis: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_nav)

//        konfetti = findViewById(R.id.viewKonfetti)
        navView = findViewById<BottomNavigationView?>(R.id.nav_view)
        navHostFragment = NavHostFragment.create(R.navigation.main_app)
        fragmentContainer = findViewById<FragmentContainerView>(R.id.nav_host_fragment)

        fragmentContainer.setOnApplyWindowInsetsListener { view, insets ->
            var consumed = false

            (view as ViewGroup).forEach { child ->
                // Dispatch the insets to the child
                val childResult = child.dispatchApplyWindowInsets(insets)
                // If the child consumed the insets, record it
                if (childResult.isConsumed) {
                    consumed = true
                }
            }

            // If any of the children consumed the insets, return
            // an appropriate value
            if (consumed) insets.consumeSystemWindowInsets() else insets
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }

        lifecycleScope.launchWhenCreated {
            newFVM.container.stateFlow.collect {
                Timber.e("\ncollecting state flow!\n")
                render(it)}
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        when (newFVM.container.stateFlow.value.goTo.equals("BottomNav")) {
            true -> setupBottomNavigationBar()
            else -> Unit
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        // Emulates installation of future on demand modules using SplitCompat.
        SplitCompat.install(this)
    }

    override fun onBackPressed() {
        val myFragment: UserTypeFragment? =
            supportFragmentManager.findFragmentByTag("onBoarding") as UserTypeFragment?
        if (myFragment != null) {
            if (myFragment.isVisible) {
                newFVM.updateFCMToken(null)
                newFVM.getAuthUIInstance()
                    .signOut(applicationContext)
                    .addOnCompleteListener { Firebase.auth.signOut() }
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        loadKoinModules(listOf(
            calendarModule,
            liveViewModule,
            recordedClipsListModule,
            settingsModule,
        ))
        val navGraphIds = listOf(R.navigation.calendar, R.navigation.live, R.navigation.history, R.navigation.settings)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = navView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment,
            intent = intent
        )

        currentNavController = controller
    }

    private fun render(state: FirebaseUIState) {
        Timber.e("The state is: ${state.goTo}")
        when (state.goTo) {
            "FirebaseUI" -> {
                navView.visibility = View.GONE
                authResultLauncher.launch(RC_SIGN_IN)
            }
            "BottomNav" -> {
                navView.visibility = View.VISIBLE
                detachNav()
                setupBottomNavigationBar()
            }
            "OnBoarding" -> {
                navView.visibility = View.GONE
                supportFragmentManager.beginTransaction()
                    .add(R.id.nav_host_fragment, navHostFragment, "OnBoarding")
                    .commitNow()
            }
        }
    }

    private val authResultLauncher = registerForActivityResult(FirebaseAuthResultContract()) { response ->
            handleResponse(response)
        }

    private fun handleResponse(response: IdpResponse?) {
        when {
            (response == null || response.error != null) -> {

                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(this, R.string.user_canceled, Toast.LENGTH_SHORT).show()
                    finish()
                }

                if (response?.error?.errorCode == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
                }

                Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_SHORT).show()
                Timber.e("Sign-in error: ${response?.error}")
            }
            else -> Unit
        }
    }

    fun detachNav() {
        supportFragmentManager.beginTransaction()
            .detach(navHostFragment)
            .commitNow()
    }

    companion object {
        private const val RC_SIGN_IN = 123
    }
}
