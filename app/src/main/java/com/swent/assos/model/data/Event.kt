package com.swent.assos.model.data

import androidx.compose.ui.graphics.Color
import com.google.firebase.firestore.DocumentSnapshot
import java.time.LocalDateTime
import java.util.Date

data class Event(
    val id: String,
    val title: String,
    val associationId: String,
    val image: String,
    val description: String,
    val date: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val documentSnapshot: DocumentSnapshot? = null,
)
