package com.swent.assos.model.data

import android.media.Image

data class News(
    val title: String,
    val association: Association,
    val Image: Image,
    val description: String,
    val date: String,
    val Event: Event? = null
)
