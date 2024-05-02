package com.swent.assos.model.data

import android.net.Uri
import com.google.firebase.firestore.DocumentSnapshot
import java.time.LocalDateTime

data class News(
    val id: String = "",
    val title: String = "",
    val associationId: String = "",
    val images: List<Uri> = mutableListOf(),
    val description: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val eventIds: MutableList<String> = mutableListOf(),
    val documentSnapshot: DocumentSnapshot? = null
)
