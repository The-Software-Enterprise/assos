package com.swent.assos.model.service.impl

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.swent.assos.model.generateUniqueID
import com.swent.assos.model.service.StorageService
import javax.inject.Inject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.single

class StorageServiceImpl @Inject constructor(private val storage: FirebaseStorage) :
    StorageService {
  @ExperimentalCoroutinesApi
  override suspend fun uploadFile(uri: Uri, ref: String): String {
    val storageRef = storage.getReference(ref + "/" + generateUniqueID())

    return callbackFlow {
          val uploadTask = storageRef.putFile(uri)
          val urlDeferred = CompletableDeferred<String>()

          uploadTask
              .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl
                    .addOnSuccessListener { uri ->
                      this.trySend(uri.toString()).isSuccess
                      close()
                    }
                    .addOnFailureListener { exception -> close(exception) }
              }
              .addOnFailureListener { exception -> close(exception) }

          awaitClose { urlDeferred.cancel() }
        }
        .single()
  }
}
