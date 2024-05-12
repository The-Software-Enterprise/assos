package com.swent.assos.model.data

import android.net.Uri
import com.google.firebase.Timestamp
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
    var staff: Map<String, Any> = mapOf(
        "userId" to "000",
        "status" to "pending",
        "createdAt" to Timestamp.now()
    ),
    val documentSnapshot: DocumentSnapshot? = null,
)
