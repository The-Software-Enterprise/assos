package com.swent.assos.model.data

import java.time.LocalDateTime

data class Applicant(
    val id: String = "",
    val userId: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val position: String = "",
    val status: String = "",
)
