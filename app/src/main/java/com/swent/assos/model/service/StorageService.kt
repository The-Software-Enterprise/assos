package com.swent.assos.model.service

import android.net.Uri

interface StorageService {
  suspend fun uploadFile(uri: Uri, ref: String): String

  suspend fun uploadFiles(uris: List<Uri>, ref: String): List<String> {
    return uris.map { uploadFile(it, ref) }
  }
}
