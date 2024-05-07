package com.swent.assos.model.data

import android.net.Uri
import java.time.LocalDateTime

data class Ticket(
    val name: String = "",
    val user: User = User(),
    var startTime: LocalDateTime? = null,
    var endTime: LocalDateTime? = null,
    val banner: Uri = Uri.EMPTY,
    val description: String = ""
)
