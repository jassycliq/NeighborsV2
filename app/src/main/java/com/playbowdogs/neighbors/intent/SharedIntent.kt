package com.playbowdogs.neighbors.intent

import android.os.Parcelable
import com.playbowdogs.neighbors.data.model.FirestoreUser
import kotlinx.parcelize.Parcelize

@Parcelize
data class SharedState(
    val user: FirestoreUser? = FirestoreUser(),
) : Parcelable

sealed class SharedEffect {
    data class IsRecording(val isRecording: Boolean) : SharedEffect()
}