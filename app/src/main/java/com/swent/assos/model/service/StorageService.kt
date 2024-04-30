package com.swent.assos.model.service

import android.net.Uri
import java.lang.Exception

interface StorageService {
  suspend fun uploadFile(
      uri: Uri,
      ref: String,
      onSuccess: (Uri) -> Unit,
      onError: (Exception) -> Unit
  )

  suspend fun uploadFiles(
      uris: List<Uri>,
      ref: String,
      onSuccess: (List<Uri>) -> Unit,
      onError: (List<Exception>) -> Unit
  ) {
    val urls = mutableListOf<Uri>()
    val errors = mutableListOf<Exception>()

    uris.forEach { uri ->
      uploadFile(uri, ref, { url -> urls.add(url) }, { error -> errors.add(error) })
    }

    if (errors.isEmpty()) {
      onSuccess(urls)
    } else {
      onError(errors)
    }
  }
}
