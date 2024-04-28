package com.swent.assos.model.data

import com.google.firebase.firestore.DocumentSnapshot
import java.time.LocalDateTime

data class Event(
    val id: String,
    var title: String = "",
    var associationId: String = "",
    var image: String = "",
    var description: String = "",
    var startTime: LocalDateTime? = null,
    var endTime: LocalDateTime? = null,
    val fields: MutableList<EventField> = mutableListOf(),
    val documentSnapshot: DocumentSnapshot? = null,
)
