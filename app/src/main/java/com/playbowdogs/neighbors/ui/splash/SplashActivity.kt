package com.playbowdogs.neighbors.ui.splash
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import com.google.android.gms.common.ConnectionResult
//import com.google.android.gms.common.GoogleApiAvailability
//import com.google.android.material.dialog.MaterialAlertDialogBuilder
//import com.google.android.play.core.appupdate.AppUpdateManager
//import com.google.android.play.core.appupdate.AppUpdateManagerFactory
//import com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED
//import com.google.android.play.core.install.model.AppUpdateType
//import com.google.android.play.core.install.model.UpdateAvailability
//import com.playbowdogs.neighbors.BuildConfig
//import com.playbowdogs.neighbors.R
//import com.playbowdogs.neighbors.di.*
//import com.playbowdogs.neighbors.ui.firebaseUI.FirebaseUIActivity
//import com.playbowdogs.neighbors.utils.ScopedAppActivity
//import com.scottyab.rootbeer.RootBeer
//import kotlinx.coroutines.*
//import timber.log.Timber
//
//@InternalCoroutinesApi
//@FlowPreview
//@ExperimentalCoroutinesApi
//class SplashActivity : ScopedAppActivity() {
//    private lateinit var rootBeer: RootBeer
//    private lateinit var appUpdateManager: AppUpdateManager
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        appUpdateManager = AppUpdateManagerFactory.create(this)
//        rootBeer = RootBeer(this)
//
//        launch {
//            Timber.e("Starting app")
//            delay(1500)
//            checkGooglePlayService()
//            checkRoot()
//            checkForUpdates()
//        }
//    }
//
//    private fun checkRoot() {
//        if (rootBeer.isRooted && !BuildConfig.DEBUG) {
//            // We found indication of root
//            showRootFoundDialog()
//            FAILED_CHECKS = true
//        }
//    }
//
//    private fun checkGooglePlayService() {
//        if (isMissingGooglePlayServicesAvailable(this@SplashActivity)) {
//            // Did not find Google Play Services (GPS)
//            showGPSMissingDialog()
//            FAILED_CHECKS = true
//        }
//    }
//
//    private fun isMissingGooglePlayServicesAvailable(activity: Activity?): Boolean {
//        val googleApiAvailability = GoogleApiAvailability.getInstance()
//        val status = googleApiAvailability.isGooglePlayServicesAvailable(activity)
//        if (status != ConnectionResult.SUCCESS) {
//            if (googleApiAvailability.isUserResolvableError(status)) {
//                googleApiAvailability.getErrorDialog(activity, status, 2404).show()
//            }
//            return true
//        }
//        return false
//    }
//
//    private fun showGPSMissingDialog() {
//        MaterialAlertDialogBuilder(this)
//            .setTitle(resources.getString(R.string.dialog_title_google_play_services))
//            .setMessage(resources.getString(R.string.dialog_message_google_play_service))
//            .setPositiveButton(resources.getString(R.string.ok)) { _, _ -> finish() }
//            .setCancelable(false)
//            .show()
//    }
//
//    private fun showRootFoundDialog() {
//        MaterialAlertDialogBuilder(this)
//            .setTitle(resources.getString(R.string.dialog_title_root_found))
//            .setMessage(resources.getString(R.string.dialog_message_root_found))
//            .setPositiveButton(resources.getString(R.string.ok)) { _, _ -> finish() }
//            .setCancelable(false)
//            .show()
//    }
//
//    private fun checkForUpdates() {
////        // Returns an intent object that you use to check for an update.
////        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
////
////        // Checks whether the platform allows the specified type of update,
////        // and checks the update priority.
////        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
////            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
////                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
////            ) {
////
////                // Request an immediate update.
////                appUpdateManager.startUpdateFlowForResult(
////                    // Pass the intent that is returned by 'getAppUpdateInfo()'.
////                    appUpdateInfo,
////                    // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
////                    AppUpdateType.IMMEDIATE,
////                    // The current activity making the update request.
////                    this,
////                    // Include a request code to later monitor this update request.
////                    RC_UPDATE)
////            } else {
//        if (!FAILED_CHECKS) {
//            val intent = Intent(this, FirebaseUIActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
////            }
////        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == RC_UPDATE) {
//            when (resultCode) {
//                RESULT_CANCELED -> {
//                    finish()
//                    Timber.e("Update flow failed!\nCanceled by user")
//                }
//                RESULT_IN_APP_UPDATE_FAILED -> {
//                    Timber.e("Update flow failed!\nExtraneous error ")
//                }
//            }
//        }
//    }
//
//    // Checks that the update is not stalled during 'onResume()'.
//    // However, you should execute this check at all entry points into the app.
//    override fun onResume() {
//        super.onResume()
//
//        appUpdateManager
//            .appUpdateInfo
//            .addOnSuccessListener { appUpdateInfo ->
//                if (appUpdateInfo.updateAvailability()
//                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
//                ) {
//                    // If an in-app update is already running, resume the update.
//                    appUpdateManager.startUpdateFlowForResult(
//                        appUpdateInfo,
//                        AppUpdateType.IMMEDIATE,
//                        this,
//                        RC_UPDATE
//                    )
//                }
//            }
//    }
//
//    companion object {
//        private const val RC_UPDATE = 122
//        private var FAILED_CHECKS = false
//    }
//}
