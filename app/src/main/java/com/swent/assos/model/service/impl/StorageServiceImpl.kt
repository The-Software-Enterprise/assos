package com.swent.assos.model.service.impl

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.swent.assos.model.generateUniqueID
import com.swent.assos.model.service.StorageService
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

class StorageServiceImpl @Inject constructor(private val storage: FirebaseStorage) :
    StorageService {
  @ExperimentalCoroutinesApi
  override suspend fun uploadFile(uri: Uri, ref: String): Uri =
      suspendCancellableCoroutine { continuation ->
        val storageRef =
            FirebaseStorage.getInstance()
                .reference
                .child(
                    ref + "/" + generateUniqueID() + "." + uri.toString().substringAfterLast('.'))
        val uploadTask = storageRef.putFile(uri)

        uploadTask
            .addOnSuccessListener { taskSnapshot ->
              taskSnapshot.storage.downloadUrl
                  .addOnSuccessListener { downloadUri -> continuation.resume(downloadUri) }
                  .addOnFailureListener { exception -> continuation.resumeWithException(exception) }
            }
            .addOnFailureListener { exception -> continuation.resumeWithException(exception) }

        continuation.invokeOnCancellation { uploadTask.cancel() }
      }
}
