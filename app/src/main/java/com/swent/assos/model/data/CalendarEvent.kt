package com.swent.assos.model.data

import androidx.compose.ui.graphics.Color
import java.time.LocalDateTime

data class CalendarEvent(
    val name: String,
    val color: Color,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val description: String? = null,
)