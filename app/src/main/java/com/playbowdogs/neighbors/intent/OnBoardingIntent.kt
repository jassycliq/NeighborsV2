package com.playbowdogs.neighbors.intent

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OnBoardingState(
    val formChecker: Boolean = false,
) : Parcelable