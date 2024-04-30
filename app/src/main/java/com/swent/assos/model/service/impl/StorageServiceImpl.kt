package com.swent.assos.model.service.impl

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.swent.assos.model.generateUniqueID
import com.swent.assos.model.service.StorageService
import java.lang.Exception
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi

class StorageServiceImpl @Inject constructor(private val storage: FirebaseStorage) :
    StorageService {
  @ExperimentalCoroutinesApi
  override suspend fun uploadFile(
      uri: Uri,
      ref: String,
      onSuccess: (Uri) -> Unit,
      onError: (Exception) -> Unit
  ) {
    val storageRef =
        storage.getReference(
            ref + "/" + generateUniqueID() + uri.toString().substringAfterLast('.', ""))

    val uploadTask = storageRef.putFile(uri)

    uploadTask.addOnSuccessListener { taskSnapshot ->
      taskSnapshot.storage.downloadUrl
          .addOnSuccessListener { uri -> onSuccess(uri) }
          .addOnFailureListener { exception -> onError(exception) }
    }
  }
}
