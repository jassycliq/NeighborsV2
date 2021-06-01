package com.playbowdogs.neighbors.utils

import android.content.Context
import android.content.Intent

object Actions {
    fun openDashboardIntent(context: Context) =
        internalIntent(context, BOTTOM_NAV_INTENT)

    fun openFirebaseUiIntent(context: Context) =
        internalIntent(context, FIREBASE_UI_NAV_INTENT)

    private fun internalIntent(context: Context, action: String) =
        Intent(action).setPackage(context.packageName)
}
