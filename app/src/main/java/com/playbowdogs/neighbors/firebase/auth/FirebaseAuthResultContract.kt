package com.playbowdogs.neighbors.firebase.auth


import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.playbowdogs.neighbors.BuildConfig
import com.playbowdogs.neighbors.R
import timber.log.Timber

class FirebaseAuthResultContract : ActivityResultContract<Int, IdpResponse>() {

    companion object {
        const val INPUT_INT = "input_int"
        val whitelistedCountries = listOf("+1")
    }

    private val providers = arrayListOf(
        AuthUI.IdpConfig.PhoneBuilder()
            .setWhitelistedCountries(whitelistedCountries)
            .build(),
    )

    override fun createIntent(context: Context, input: Int?): Intent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAlwaysShowSignInMethodScreen(true)
        .setIsSmartLockEnabled(!BuildConfig.DEBUG, true)
        .setAvailableProviders(providers)
        .setTheme(R.style.LoginTheme)
        .setLogo(R.drawable.ic_playbow_logo_full_color)
        .build().apply {
            putExtra(INPUT_INT, input)
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP and Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

    override fun parseResult(resultCode: Int, intent: Intent?): IdpResponse? {
        Timber.e("In parseResult")
        if (resultCode != Activity.RESULT_OK) {
            return null
        }
        return IdpResponse.fromResultIntent(intent)
    }
}
