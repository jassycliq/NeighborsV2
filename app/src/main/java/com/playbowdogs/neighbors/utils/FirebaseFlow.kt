package com.playbowdogs.neighbors.utils

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber


@ExperimentalCoroutinesApi
fun CollectionReference.getQuerySnapshotFlow(): Flow<QuerySnapshot?> {
    return callbackFlow {
        val listenerRegistration =
                addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        cancel(
                                message = "error fetching collection data at path - $path",
                                cause = firebaseFirestoreException
                        )
                        return@addSnapshotListener
                    }
                    offer(querySnapshot)
                }
        awaitClose {
            Timber.e("cancelling the listener on collection at path - $path")
            listenerRegistration.remove()
        }
    }
}

@ExperimentalCoroutinesApi
fun <T> CollectionReference.getDataFlow(mapper: (QuerySnapshot?) -> T): Flow<T>? {
    return getQuerySnapshotFlow()
        .map {
            return@map mapper(it)
        }
}

@ExperimentalCoroutinesApi
fun DocumentReference.getQuerySnapshotFlow(): Flow<DocumentSnapshot?> {
    return callbackFlow {
        val listenerRegistration =
                addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        cancel(
                                message = "error fetching collection data at path - $path",
                                cause = firebaseFirestoreException
                        )
                        return@addSnapshotListener
                    }
                    offer(querySnapshot)
                }
        awaitClose {
            Timber.e("cancelling the listener on collection at path - $path")
            listenerRegistration.remove()
        }
    }
}

@ExperimentalCoroutinesApi
fun <T> DocumentReference.getDataFlow(mapper: (DocumentSnapshot?) -> T): Flow<T> {
    return getQuerySnapshotFlow()
            .map {
                return@map mapper(it)
            }
}
