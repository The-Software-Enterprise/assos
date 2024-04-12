package com.swent.assos.model.data

import java.util.Date

data class News(
    val id: String = "",
    val title: String = "",
    val associationId: String = "",
    val image: String = "",
    val description: String = "",
    val date: Date = Date(),
    val eventId: String = ""
)
