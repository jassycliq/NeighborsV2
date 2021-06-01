package com.playbowdogs.neighbors.intent

import android.os.Parcelable
import com.google.firebase.auth.FirebaseUser
import com.playbowdogs.neighbors.data.model.FirestoreUser
import kotlinx.parcelize.Parcelize

@Parcelize
data class FirebaseUIState(
    val firestoreUser: FirestoreUser? = FirestoreUser(),
    val goTo: String? = null,
    val firebaseUser: FirebaseUser? = null,
    ) : Parcelable

//sealed class FirebaseUIEffect {
//    data class NextButton(val buttonEnabled: Boolean? = false) : FirebaseUIEffect()
//    data class SubmitButton(val buttonEnabled: Boolean? = false) : FirebaseUIEffect()
//}
