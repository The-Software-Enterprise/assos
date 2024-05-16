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
    var startTime: LocalDateTime? = null,
    var endTime: LocalDateTime? = null,
    val documentSnapshot: DocumentSnapshot? = null,
)
