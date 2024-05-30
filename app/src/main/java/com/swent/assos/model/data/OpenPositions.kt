package com.swent.assos.model.data

import com.google.firebase.firestore.DocumentSnapshot

data class OpenPositions(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var requirements: List<String> = listOf(),
    var responsibilities: List<String> = listOf(),
    val documentSnapshot: DocumentSnapshot? = null
)
