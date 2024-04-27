package com.swent.assos.model.data

import com.google.firebase.firestore.DocumentSnapshot
import java.time.LocalDateTime

data class Event(
    val id: String,
    val title: String,
    val associationId: String,
    val image: String,
    val description: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val fields: List<EventField>,
    val documentSnapshot: DocumentSnapshot? = null,
)
