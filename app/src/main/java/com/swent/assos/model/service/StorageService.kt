package com.swent.assos.model.service

import android.net.Uri
import java.lang.Exception
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

interface StorageService {
  suspend fun uploadFile(
      uri: Uri,
      ref: String,
  ): Uri

  suspend fun uploadFiles(
      uris: List<Uri>,
      ref: String,
      onSuccess: (List<Uri>) -> Unit,
      onError: (List<Exception>) -> Unit
  ) {
    val urls = mutableListOf<Uri>()
    val errors = mutableListOf<Exception>()

    coroutineScope {
      val jobs =
          uris.map { uri ->
            async {
              try {
                val url = uploadFile(uri, ref)
                synchronized(urls) { urls.add(url) }
              } catch (e: Exception) {
                synchronized(errors) { errors.add(e) }
              }
            }
          }
      jobs.awaitAll()
    }

    if (errors.isEmpty()) {
      onSuccess(urls)
    } else {
      onError(errors)
    }
  }
}
