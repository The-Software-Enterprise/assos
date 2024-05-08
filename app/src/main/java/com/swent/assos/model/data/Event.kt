package com.swent.assos.model.data

import android.net.Uri
import com.google.firebase.firestore.DocumentSnapshot
import java.time.LocalDateTime

data class Event(
    val id: String,
    val title: String = "",
    var associationId: String = "",
    var image: Uri = Uri.EMPTY,
    val description: String = "",
    var startTime: LocalDateTime = LocalDateTime.now(),
    var endTime: LocalDateTime = LocalDateTime.now().plusHours(1),
    val fields: List<Field> = emptyList(),
    val documentSnapshot: DocumentSnapshot? = null,
) {
  sealed class Field {
    data class Text(var title: String, var text: String) : Field()

    data class Image(val uris: List<Uri>) : Field()
  }

  enum class FieldType(val type: String) {
    TEXT("text"),
    IMAGE("image"),
  }
}
