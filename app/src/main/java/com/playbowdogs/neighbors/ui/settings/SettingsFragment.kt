package com.playbowdogs.neighbors.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.playbowdogs.neighbors.R
import com.playbowdogs.neighbors.firebase.firestore.FirestoreViewModel
import com.playbowdogs.neighbors.utils.Actions
import com.playbowdogs.neighbors.viewmodel.SharedViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@FlowPreview
@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class SettingsFragment : PreferenceFragmentCompat() {
    private val firestoreViewModel: FirestoreViewModel by sharedViewModel()
    private val sharedViewModel: SharedViewModel by sharedViewModel()
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
            sharedViewModel.updateFCMToken(null)
            sharedPrefEdit.clear().apply()
            sharedViewModel.authUIInstance
                .signOut(requireContext())
                .addOnCompleteListener {
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
