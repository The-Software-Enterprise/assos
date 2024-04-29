package com.swent.assos.model.data

import com.google.firebase.firestore.DocumentSnapshot
import java.util.Date

data class News(
    val id: String = "",
    val title: String = "",
    val associationId: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val date: Date = Date(),
    val eventId: String = "",
    val documentSnapshot: DocumentSnapshot? = null
)
