package com.swent.assos.model.data

import com.google.firebase.firestore.DocumentSnapshot

data class Association(
    val acronym: String,
    val fullname: String,
    val url: String,
    val documentSnapshot: DocumentSnapshot? = null,
    val description: String
)
