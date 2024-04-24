package com.swent.assos.model.data

import com.google.firebase.firestore.DocumentSnapshot

data class Event(
    val id: String,
    val title: String,
    val associationId: String,
    val image: String,
    val description: String,
    val date: String,
    val documentSnapshot: DocumentSnapshot? = null,
)
