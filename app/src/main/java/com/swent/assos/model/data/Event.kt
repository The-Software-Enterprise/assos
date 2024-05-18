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
    var fields: List<Field> = emptyList(),
    var isStaffingEnabled: Boolean = false,
    var documentSnapshot: DocumentSnapshot? = null,

    // Use to listen change in state
    val _unused: Boolean = false,
) {
  sealed class Field {
    data class Text(var title: String, var text: String) : Field()

    data class Image(val uris: List<Uri>) : Field()
  }
}
