package com.playbowdogs.neighbors.intent

import android.os.Parcelable
import com.playbowdogs.neighbors.data.model.FirestoreUser
import kotlinx.parcelize.Parcelize

@Parcelize
data class FirestoreState(
    val firestoreUser: FirestoreUser? = FirestoreUser(),
) : Parcelable
