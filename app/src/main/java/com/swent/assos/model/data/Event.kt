package com.swent.assos.model.data

import android.media.Image

data class Event(
    val title: String,
    val association: Association,
    val image: Image?,
    val description: String,
    val date: String
)
