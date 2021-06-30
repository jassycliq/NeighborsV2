package com.playbowdogs.neighbors.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.firebase.ui.auth.AuthUI
import com.playbowdogs.neighbors.R
import com.playbowdogs.neighbors.di.calendarModule
import com.playbowdogs.neighbors.di.liveViewModule
import com.playbowdogs.neighbors.di.recordedClipsListModule
import com.playbowdogs.neighbors.di.settingsModule
import com.playbowdogs.neighbors.firebase.firestore.SettingsViewModel
import com.playbowdogs.neighbors.utils.Actions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

@FlowPreview
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class SettingsFragment : PreferenceFragmentCompat() {
    private val settingsViewModel: SettingsViewModel by sharedViewModel()
//    private val sharedViewModel: SharedViewModel by sharedViewModel()
    private val sharedPrefEdit by inject<SharedPreferences.Editor>()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInsetPadding(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val signOutPreference: Preference? = findPreference("signOut")

        signOutPreference?.setOnPreferenceClickListener {
            settingsViewModel.updateFCMToken(null)
            sharedPrefEdit.clear().apply()
            settingsViewModel.authInstance
                .signOut(requireContext())
                .addOnCompleteListener {
                    unloadKoinModules(listOf(
                        calendarModule,
                        liveViewModule,
                        recordedClipsListModule,
                        settingsModule,
                    ))
                    startActivity(Actions.openFirebaseUiIntent(requireContext()))
                    activity?.finish()
                }
            true
        }
    }

    private fun setInsetPadding(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _view, insets ->
            // Move our Preference Fragment below status bar
            _view.updatePadding(top = insets.systemWindowInsetTop)
            insets
        }
    }
}
