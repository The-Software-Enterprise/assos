package com.swent.assos.model.data

import android.net.Uri
import com.google.firebase.firestore.DocumentSnapshot

data class Association(
    val id: String = "",
    val acronym: String = "",
    val fullname: String = "",
    val url: String = "",
    val documentSnapshot: DocumentSnapshot? = null,
    val logo: Uri = Uri.EMPTY,
    val description: String = ""
)
